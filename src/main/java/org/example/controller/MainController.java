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

import java.util.List;

public class MainController {

    @FXML private TableView<Chanson> tableChansons;
    @FXML private TableColumn<Chanson, String> colTitre;
    @FXML private TableColumn<Chanson, String> colArtiste;
    @FXML private TableColumn<Chanson, String> colAlbum;
    @FXML private TableColumn<Chanson, Integer> colAnnee;
    @FXML private TableColumn<Chanson, String> colGenre;
    @FXML private TableColumn<Chanson, Integer> colDuree;
    @FXML private TableColumn<Chanson, Integer> colEcoutes;

    @FXML private ListView<Playlist> listePlaylists;
    @FXML private Label chansonActuelle;

    private Bibliotheque bibliotheque;
    private ServicePlaylist servicePlaylist;
    private LecteurSimule lecteurSimule;

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
        lecteurSimule = new LecteurSimule(bibliotheque);

        tableChansons.setItems(FXCollections.observableArrayList(chansons));
    }

    @FXML
    private void creerPlaylist() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.showAndWait().ifPresent(nom -> {
            servicePlaylist.creerPlaylist(nom);
            listePlaylists.setItems(
                    FXCollections.observableArrayList(bibliotheque.getPlaylists())
            );
        });
    }

    @FXML
    private void ajouterPlaylist() {
        Playlist playlist = listePlaylists.getSelectionModel().getSelectedItem();
        Chanson chanson = tableChansons.getSelectionModel().getSelectedItem();

        if (playlist != null && chanson != null) {
            servicePlaylist.ajouterChanson(playlist, chanson);
        }
    }

    @FXML
    private void retirerPlaylist() {
        Playlist playlist = listePlaylists.getSelectionModel().getSelectedItem();
        Chanson chanson = tableChansons.getSelectionModel().getSelectedItem();

        if (playlist != null && chanson != null) {
            servicePlaylist.retirerChanson(playlist, chanson);
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