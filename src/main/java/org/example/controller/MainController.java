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
import org.example.util.LecteurBD;
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
import org.example.dao.ChansonDAO;
import org.example.dao.ChansonDAOImpl;
import javafx.scene.layout.GridPane;

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
    private Slider sliderDuree;
    @FXML
    private Label labelPage;
    @FXML
    private Slider sliderEcoutes;
    @FXML
    private Label labelGagnant;
    private Timeline timeline;

    private ServiceRecherche serviceRecherche;
    private Bibliotheque bibliotheque;
    private ChansonDAO chansonDAO;
    private ServicePlaylist servicePlaylist;
    private LecteurSimule lecteurSimule;
    private ServiceFiltre serviceFiltre;
    private int pageActuelle = 1;
    private final int taillePage = 25;
    private ServicePagination servicePagination;
    private List<Chanson> chansonsAffichees;
    private ServiceTri serviceTri;
    private ServiceBenchmark serviceBenchmark;


    @FXML
    public void initialize() {
        chansonDAO = new ChansonDAOImpl();
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colArtiste.setCellValueFactory(new PropertyValueFactory<>("artiste"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colEcoutes.setCellValueFactory(new PropertyValueFactory<>("nbr_ecoute"));
        LecteurBD lecteur = new LecteurBD();
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
        chansonsAffichees = bibliotheque.getChansons();
        pageActuelle = 1;
        afficherPage();
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
            appliquerFiltres();
        });

        sliderDuree.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            appliquerFiltres();
        });

        sliderEcoutes.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            appliquerFiltres();
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
            if (event.getClickCount()== 2) {
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
        dialog.setContentText("Nom:");
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

            if (choix.equals("Titre")){
                comparateur = serviceTri.parTitre();
            } else if (choix.equals("Artiste")) {
                comparateur = serviceTri.parArtiste();
            } else if (choix.equals("Durée")){
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
            labelGagnant.setText("Gagnant : " + gagnant.getNomAlgorithme());
            tableChansons.setItems(
                    FXCollections.observableArrayList(gagnant.getChansons())
            );
        }
    @FXML
    private void afficherBenchmark() {
        Comparator<Chanson> comparateur = serviceTri.parTitre();
        List<ResultatTri> tris = serviceTri.trierAvecLesTroisAlgorithmes(
                bibliotheque.getChansons(),
                comparateur
        );

        List<ResultatBenchmark> resultats =
                serviceBenchmark.obtenirResultatsBenchmark(tris);
        String texte = "";
        for (ResultatBenchmark resultat : resultats) {
            texte += resultat.getNomAlgorithme()
                    + " : "
                    + resultat.getTempsMillisecondes()
                    + " ms\n";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Benchmark");
        alert.setContentText(texte);
        alert.show();
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
                    "Écoutes: " +chanson.getNbr_ecoute()
            );
        }
    }

    private void afficherPage() {
        int debut = (pageActuelle - 1) * taillePage;
        int fin = Math.min(debut + taillePage, chansonsAffichees.size());

        tableChansons.setItems(
                FXCollections.observableArrayList(
                        chansonsAffichees.subList(debut, fin)
                )
        );

        labelPage.setText("Page " + pageActuelle);
    }

    @FXML
    private void pageSuivante() {

        int nombrePages = servicePagination.nombrePages(
                chansonsAffichees, taillePage
        );
        if (pageActuelle < nombrePages) {
            pageActuelle++;
            afficherPage();
        }
    }
    @FXML
    private void pagePrecedente() {

        if (pageActuelle > 1) {
            pageActuelle--;
            afficherPage();
        }
    }


    private void appliquerFiltres() {
        Genre genre = null;
        if (!comboGenre.getValue().equals("Tous")) {
            genre = Genre.valueOf(comboGenre.getValue());
        }
        int dureeMax = (int) sliderDuree.getValue();
        int ecoutesMin = (int) sliderEcoutes.getValue();
        chansonsAffichees = serviceFiltre.filtrerChansons(
                genre,
                dureeMax,
                ecoutesMin,
                null
        );

        pageActuelle = 1;
        afficherPage();
    }
    @FXML
    private void ajouterChanson() {
        Dialog<Chanson> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une chanson");
        ButtonType ajouterButton =
                new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(
                ajouterButton,
                ButtonType.CANCEL
        );
        TextField titre = new TextField();
        TextField artiste = new TextField();
        TextField album = new TextField();
        TextField annee = new TextField();
        ComboBox<Genre> genre = new ComboBox<>();
        TextField duree = new TextField();
        TextField ecoutes = new TextField();
        genre.getItems().addAll(Genre.values());

        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(10);
        grille.add(new Label("Titre :"), 0, 0);
        grille.add(titre, 1, 0);
        grille.add(new Label("Artiste :"), 0, 1);
        grille.add(artiste, 1, 1);
        grille.add(new Label("Album :"), 0, 2);
        grille.add(album, 1, 2);
        grille.add(new Label("Année :"), 0, 3);
        grille.add(annee, 1, 3);
        grille.add(new Label("Genre :"), 0, 4);
        grille.add(genre, 1, 4);
        grille.add(new Label("Durée :"), 0, 5);
        grille.add(duree, 1, 5);
        grille.add(new Label("Écoutes :"), 0, 6);
        grille.add(ecoutes, 1, 6);
        dialog.getDialogPane().setContent(grille);
        dialog.setResultConverter(button -> {

            if (button == ajouterButton) {
                try {
                    if (titre.getText().isBlank()
                            || artiste.getText().isBlank()
                            || album.getText().isBlank()
                            || genre.getValue() == null) {
                        new Alert(
                                Alert.AlertType.ERROR,
                                "Tous les champs sont obligatoires."
                        ).show();
                        return null;
                    }

                    int anneeValeur = Integer.parseInt(annee.getText());
                    int dureeValeur = Integer.parseInt(duree.getText());
                    int ecoutesValeur = Integer.parseInt(ecoutes.getText());
                    if (anneeValeur < 1900
                            || dureeValeur <= 0
                            || ecoutesValeur < 0) {
                        new Alert(
                                Alert.AlertType.ERROR,
                                "Valeurs invalides."
                        ).show();
                        return null;
                    }

                    return new Chanson(
                            0,
                            titre.getText(),
                            artiste.getText(),
                            album.getText(),
                            anneeValeur,
                            genre.getValue(),
                            dureeValeur,
                            ecoutesValeur
                    );
                } catch (NumberFormatException e) {
                    new Alert(
                            Alert.AlertType.ERROR,
                            "Année, durée et écoutes doivent être des nombres."
                    ).show();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(chanson -> {
            try {
                chansonDAO.ajouter(chanson);
                List<Chanson> chansons = chansonDAO.trouverTous();
                bibliotheque = new Bibliotheque(chansons);
                chansonsAffichees = bibliotheque.getChansons();
                pageActuelle = 1;
                afficherPage();
            } catch (Exception e) {
                new Alert(
                        Alert.AlertType.ERROR,
                        "Erreur lors de l'ajout."
                ).show();
            }
        });
    }

    @FXML
    private void modifierChanson() {
        Chanson chanson = tableChansons.getSelectionModel().getSelectedItem();
        if (chanson == null) {
            return;
        }
        Dialog<Chanson> dialog = new Dialog<>();
        dialog.setTitle("Modifier une chanson");

        ButtonType modifierButton = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButton, ButtonType.CANCEL);
        TextField titre = new TextField(chanson.getTitre());
        TextField artiste = new TextField(chanson.getArtiste());
        TextField album = new TextField(chanson.getAlbum());
        TextField annee = new TextField(String.valueOf(chanson.getAnnee()));
        TextField duree = new TextField(String.valueOf(chanson.getDuree()));
        TextField ecoutes = new TextField(String.valueOf(chanson.getNbr_ecoute()));

        ComboBox<Genre> genre = new ComboBox<>();
        genre.getItems().addAll(Genre.values());
        genre.setValue(chanson.getGenre());
        GridPane grille = new GridPane();
        grille.setHgap(8);
        grille.setVgap(5);
        grille.add(new Label("Titre"), 0, 0);
        grille.add(titre, 1, 0);
        grille.add(new Label("Artiste"), 0, 1);
        grille.add(artiste, 1, 1);
        grille.add(new Label("Album"), 0, 2);
        grille.add(album, 1, 2);
        grille.add(new Label("Année"), 0, 3);
        grille.add(annee, 1, 3);
        grille.add(new Label("Genre"), 0, 4);
        grille.add(genre, 1, 4);
        grille.add(new Label("Durée"), 0, 5);
        grille.add(duree, 1, 5);
        grille.add(new Label("Écoutes"), 0, 6);
        grille.add(ecoutes, 1, 6);

        dialog.getDialogPane().setContent(grille);
        dialog.setResultConverter(button -> {
            if (button == modifierButton) {
                try {
                    int anneeValeur = Integer.parseInt(annee.getText());
                    int dureeValeur = Integer.parseInt(duree.getText());
                    int ecoutesValeur = Integer.parseInt(ecoutes.getText());
                    if (titre.getText().isBlank()
                            || artiste.getText().isBlank()
                            || album.getText().isBlank()
                            || genre.getValue() == null
                            || anneeValeur < 1900
                            || dureeValeur <= 0
                            || ecoutesValeur < 0) {
                        new Alert(Alert.AlertType.ERROR, "Valeurs invalides").show();
                        return null;
                    }
                    return new Chanson(
                            chanson.getId(),
                            titre.getText(),
                            artiste.getText(),
                            album.getText(),
                            anneeValeur,
                            genre.getValue(),
                            dureeValeur,
                            ecoutesValeur
                    );

                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Valeurs invalides").show();
                }
            }
            return null;
        });
        dialog.showAndWait().ifPresent(chansonModifiee -> {
            try {
                chansonDAO.modifier(chansonModifiee);
                List<Chanson> chansons = chansonDAO.trouverTous();
                bibliotheque = new Bibliotheque(chansons);
                chansonsAffichees = bibliotheque.getChansons();
                pageActuelle = 1;
                afficherPage();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de la modification").show();
            }
        });
    }




    @FXML
    private void supprimerChanson() {
        Chanson chanson = tableChansons.getSelectionModel().getSelectedItem();
        if (chanson == null) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Supprimer cette chanson?"
        );
        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                try {
                    chansonDAO.supprimer(chanson.getId());
                    List<Chanson> chansons = chansonDAO.trouverTous();
                    bibliotheque = new Bibliotheque(chansons);
                    chansonsAffichees = bibliotheque.getChansons();
                    afficherPage();
                } catch (Exception e) {
                    new Alert(
                            Alert.AlertType.ERROR,
                            "Erreur lors de la suppression."
                    ).show();
                }
            }
        });
    }
}
