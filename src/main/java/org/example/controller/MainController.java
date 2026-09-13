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
import org.example.service.ServicePagination;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.example.model.ResultatTri;
import org.example.model.ResultatBenchmark;
import org.example.service.ServiceTri;
import org.example.service.ServiceBenchmark;


public class MainController {

    @FXML
    private TableView<Chanson> tableChansons;
    @FXML
    private TableColumn<Chanson, String> colTitre;
    @FXML
    private TableColumn<Chanson, String> colArtiste;
    @FXML
    private TableColumn<Chanson, String> colAlbum;
    @FXML
    private TableColumn<Chanson, Integer> colAnnee;
    @FXML
    private TableColumn<Chanson, String> colGenre;
    @FXML
    private TableColumn<Chanson, Integer> colDuree;
    @FXML
    private TableColumn<Chanson, Integer> colEcoutes;
    @FXML
    private TextField champRecherche;
    @FXML
    private ComboBox<String> comboRecherche;
    @FXML
    private ComboBox<String> comboGenre;
    @FXML
    private ComboBox<String> comboTri;
    @FXML
    private ListView<Playlist> listePlaylists;
    @FXML
    private Label chansonActuelle;
    @FXML
    private ProgressBar progressionChanson;
    @FXML
    private Label ecoutesChanson;
    @FXML
    private Label labelPage;
    private Timeline timeline;

    private ServiceRecherche serviceRecherche;
    private Bibliotheque bibliotheque;
    private ServicePlaylist servicePlaylist;
    private LecteurSimule lecteurSimule;
    private ServiceFiltre serviceFiltre;
    private int pageActuelle = 1;
    private final int taillePage = 25;
    private ServicePagination servicePagination;
    private ServiceTri serviceTri;
    private ServiceBenchmark serviceBenchmark;

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
        serviceTri = new ServiceTri();
        serviceBenchmark = new ServiceBenchmark();
        lecteurSimule = new LecteurSimule(bibliotheque);
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            lecteurSimule.avancerTemps(1);
                            double progression = lecteurSimule.getProgression();
                            progressionChanson.setProgress(progression);
                            afficherChanson();
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        servicePagination = new ServicePagination();
        int fin = Math.min(taillePage, chansons.size());

        tableChansons.setItems(
                FXCollections.observableArrayList(
                        chansons.subList(0, fin)
                )
        );
        pageActuelle = 1;
        labelPage.setText("Page 1");
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
                "Artiste", "Durée", "Année", "Écoutes", "Genre"
        );
        comboRecherche.getItems().addAll(
                "Tous", "Titre", "Artiste"
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
        tableChansons.getSelectionModel().selectedItemProperty().addListener(
                (observable, ancienneChanson, nouvelleChanson) -> {

                    if (nouvelleChanson != null) {
                        lecteurSimule.choisirChanson(nouvelleChanson);
                        progressionChanson.setProgress(0);
                        afficherChanson();
                    }
                }
        );
    }

    @FXML
    private void afficherBibliotheque() {
        pageActuelle = 1;
        List<Chanson> chansons = bibliotheque.getChansons();
        int fin = Math.min(taillePage, chansons.size());
        tableChansons.setItems(
                FXCollections.observableArrayList(
                        chansons.subList(0, fin)
                )
        );
        labelPage.setText("Page 1");
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

        pageActuelle = 1;
        List<Chanson> chansons = bibliotheque.getChansons();
        int fin = Math.min(taillePage, chansons.size());
        tableChansons.setItems(
                FXCollections.observableArrayList(
                        chansons.subList(0, fin)
                )
        );

        labelPage.setText("Page 1");

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
    private void supprimerPlaylist() {
        Playlist playlist = listePlaylists.getSelectionModel().getSelectedItem();

        if (playlist != null) {
            servicePlaylist.supprimerPlaylist(playlist);

            listePlaylists.setItems(
                    FXCollections.observableArrayList(
                            bibliotheque.getPlaylists()
                    )
            );
        }
    }

        @FXML
        private void lancerTri() {
            String choix = comboTri.getValue();

            if (choix == null) return;

            Comparator<Chanson> comparateur = null;

            if (choix.equals("Titre")) {
                comparateur = serviceTri.parTitre();
            } else if (choix.equals("Artiste")) {
                comparateur = serviceTri.parArtiste();
            } else if (choix.equals("Durée")) {
                comparateur = serviceTri.parDuree();
            } else if (choix.equals("Année")) {
                comparateur = serviceTri.parAnnee();
            } else if (choix.equals("Écoutes")) {
                comparateur = serviceTri.parEcoutes();
            } else if (choix.equals("Genre")) {
                comparateur = serviceTri.parGenre();
            }

            List<ResultatTri> resultats =
                    serviceTri.trierAvecLesTroisAlgorithmes(
                            bibliotheque.getChansons(),
                            comparateur
                    );

            ResultatTri gagnant = serviceTri.trouverLePlusRapide(resultats);

            tableChansons.setItems(
                    FXCollections.observableArrayList(gagnant.getChansons())
            );
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
            controller.setMainController(this);
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
        timeline.play();
        afficherChanson();
    }

    @FXML
    private void pause() {
        lecteurSimule.pause();
        timeline.pause();
    }
    @FXML
    private void suivant() {
        lecteurSimule.suivant();
        progressionChanson.setProgress(0);
        afficherChanson();
        tableChansons.refresh();
    }

    @FXML
    private void precedent() {
        lecteurSimule.precedent();
        progressionChanson.setProgress(0);
        afficherChanson();
    }

    @FXML
    private void shuffle() {
        lecteurSimule.shuffle();
        progressionChanson.setProgress(0);
        afficherChanson();
    }

    public void selectionnerChanson(Chanson chanson) {
        lecteurSimule.choisirChanson(chanson);
        progressionChanson.setProgress(0);
        afficherChanson();
    }
    private void afficherChanson() {
        Chanson chanson = lecteurSimule.getChansonActuelle();
        if (chanson != null) {
            chansonActuelle.setText(
                    chanson.getTitre() + " - " + chanson.getArtiste()
            );
            ecoutesChanson.setText(
                    "Écoutes : " + chanson.getNbr_ecoute()
            );
        }
    }

    @FXML
    private void pageSuivante() {
        int nombrePages = servicePagination.nombrePages(
                bibliotheque.getChansons(), taillePage
        );

        if (pageActuelle < nombrePages) {
            pageActuelle++;
            int debut = (pageActuelle - 1) * taillePage;
            int fin = Math.min(
                    debut + taillePage,
                    bibliotheque.getChansons().size()
            );
            tableChansons.setItems(
                    FXCollections.observableArrayList(
                            bibliotheque.getChansons().subList(debut, fin)
                    )
            );

            labelPage.setText("Page " + pageActuelle);
        }
    }

    @FXML
    private void pagePrecedente() {
        if (pageActuelle > 1) {
            pageActuelle--;
            int debut = (pageActuelle - 1) * taillePage;
            int fin = Math.min(
                    debut + taillePage,
                    bibliotheque.getChansons().size()
            );
            tableChansons.setItems(
                    FXCollections.observableArrayList(
                            bibliotheque.getChansons().subList(debut, fin)
                    )
            );
            labelPage.setText("Page " + pageActuelle);
        }
    }
}