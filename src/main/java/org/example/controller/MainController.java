package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.model.Bibliotheque;
import org.example.model.Chanson;
import org.example.model.Playlist;
import org.example.service.LecteurSimule;
import org.example.service.ServicePlaylist;
import org.example.util.LecteurCSV;
import org.example.service.ServiceRecherche;
import java.util.List;
import org.example.model.Genre;
import org.example.service.ServiceFiltre;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Comparator;


public class MainController {

    @FXML private TableView<Chanson> tableChansons;
    @FXML private TableColumn<Chanson, String> colTitre;
    @FXML private TableColumn<Chanson, String> colArtiste;
    @FXML private TableColumn<Chanson, String> colAlbum;
    @FXML private TableColumn<Chanson, Integer> colAnnee;
    @FXML private TableColumn<Chanson, String> colGenre;
    @FXML private TableColumn<Chanson, Integer> colDuree;
    @FXML private TableColumn<Chanson, Integer> colEcoutes;
    @FXML private TextField champRecherche;
    @FXML private ComboBox<String> comboRecherche;
    @FXML private ComboBox<String> comboGenre;
    @FXML private ComboBox<String> comboTri;
    @FXML private ListView<Playlist> listePlaylists;
    @FXML private Label chansonActuelle;


    private ServiceRecherche serviceRecherche;
    private Bibliotheque bibliotheque;
    private ServicePlaylist servicePlaylist;
    private LecteurSimule lecteurSimule;
    private ServiceFiltre serviceFiltre;

    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colArtiste.setCellValueFactory(new PropertyValueFactory<>("artiste"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colEcoutes.setCellValueFactory(new PropertyValueFactory<>("nbr_ecoute"));

        LecteurCSV lecteur = new LecteurCSV("src/main/resources/data/chansons.csv");
        List<Chanson> chansons = lecteur.charger();

        bibliotheque = new Bibliotheque(chansons);

        servicePlaylist = new ServicePlaylist(bibliotheque);
        serviceRecherche = new ServiceRecherche(bibliotheque);
        serviceFiltre = new ServiceFiltre(bibliotheque);
        lecteurSimule = new LecteurSimule(bibliotheque);

        tableChansons.setItems(
                FXCollections.observableArrayList(chansons)
        );

        listePlaylists.setCellFactory(liste -> new ListCell<>() {
            @Override
            protected void updateItem(Playlist playlist, boolean empty) {
                super.updateItem(playlist, empty);

                if (empty || playlist == null) {
                    setText(null);
                } else {
                    setText(playlist.getNom());
                }
            }
        });

        comboGenre.getItems().add("Tous");

        for (Genre genre : Genre.values()) {
            comboGenre.getItems().add(genre.name());
        }

        comboGenre.getSelectionModel().selectFirst();

        comboTri.getItems().addAll(
                "Titre",
                "Artiste",
                "Durée",
                "Année",
                "Écoutes"
        );

        comboRecherche.getItems().addAll(
                "Tous",
                "Titre",
                "Artiste"
        );

        comboRecherche.getSelectionModel().selectFirst();


        comboGenre.setOnAction(event -> {
            String choix = comboGenre.getValue();

            if (choix.equals("Tous")) {
                tableChansons.setItems(FXCollections.observableArrayList(bibliotheque.getChansons()));
            } else {
                Genre genre = Genre.valueOf(choix);

                tableChansons.setItems(FXCollections.observableArrayList(
                        serviceFiltre.filtrerChansons(genre, null, null, null)
                ));
            }
        });
        champRecherche.textProperty().addListener((observable, ancienTexte, nouveauTexte) -> {

            String choix = comboRecherche.getValue();

            List<Chanson> resultat = bibliotheque.getChansons().stream()
                    .filter(chanson -> {

                        if (choix.equals("Titre")) {
                            return chanson.getTitre()
                                    .toLowerCase()
                                    .contains(nouveauTexte.toLowerCase());
                        }

                        if (choix.equals("Artiste")) {
                            return chanson.getArtiste()
                                    .toLowerCase()
                                    .contains(nouveauTexte.toLowerCase());
                        }

                        return chanson.getTitre()
                                .toLowerCase()
                                .contains(nouveauTexte.toLowerCase())
                                || chanson.getArtiste()
                                .toLowerCase()
                                .contains(nouveauTexte.toLowerCase());
                    })
                    .toList();

            tableChansons.setItems(
                    FXCollections.observableArrayList(resultat)
            );
            });
        listePlaylists.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ouvrirPlaylist();
            }
        });
    }

    @FXML
    private void creerPlaylist() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Nouvelle playlist");
        dialog.setHeaderText("Créer une playlist");
        dialog.setContentText("Nom :");
        dialog.showAndWait().ifPresent(nom -> {
            servicePlaylist.creerPlaylist(nom);

            listePlaylists.setItems(
                    FXCollections.observableArrayList(bibliotheque.getPlaylists())
            );

            listePlaylists.getSelectionModel().selectLast();
        });
    }

    @FXML
    private void ajouterPlaylist() {
        Playlist playlist = listePlaylists.getSelectionModel().getSelectedItem();

        if (playlist == null) {
            return;
        }

        tableChansons.setItems(
                FXCollections.observableArrayList(bibliotheque.getChansons())
        );

        tableChansons.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Chanson chanson = tableChansons.getSelectionModel().getSelectedItem();

                if (chanson != null) {
                    servicePlaylist.ajouterChanson(playlist, chanson);

                    tableChansons.setItems(
                            FXCollections.observableArrayList(playlist.getChansons())
                    );

                    tableChansons.setOnMouseClicked(null);
                }
            }
        });
    }
    @FXML
    private void retirerPlaylist() {
        Playlist playlist = listePlaylists.getSelectionModel().getSelectedItem();
        Chanson chanson = tableChansons.getSelectionModel().getSelectedItem();

        if (playlist != null && chanson != null) {
            servicePlaylist.retirerChanson(playlist, chanson);

            tableChansons.setItems(
                    FXCollections.observableArrayList(playlist.getChansons())
            );
        }
    }


    @FXML
    private void ouvrirPlaylist() {
        Playlist playlist = listePlaylists.getSelectionModel().getSelectedItem();

        if (playlist == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/playlist-view.fxml")
            );

            Parent root = loader.load();

            PlaylistController controller = loader.getController();
            controller.setPlaylist(playlist, servicePlaylist);

            Stage stage = new Stage();
            stage.setTitle(playlist.getNom());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void play() {
        lecteurSimule.play();
        afficherChanson();
    }

    @FXML
    private void pause() {
        lecteurSimule.pause();
    }

    @FXML
    private void suivant() {
        lecteurSimule.suivant();
        afficherChanson();
        tableChansons.refresh();
    }

    @FXML
    private void precedent() {
        lecteurSimule.precedent();
        afficherChanson();
    }

    @FXML
    private void shuffle() {
        lecteurSimule.shuffle();
        afficherChanson();
    }

    private void afficherChanson() {
        Chanson chanson = lecteurSimule.getChansonActuelle();

        if (chanson != null) {
            chansonActuelle.setText(chanson.getTitre() + " - " + chanson.getArtiste());
        }
    }

}