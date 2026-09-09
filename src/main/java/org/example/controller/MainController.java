package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.model.Chanson;
import org.example.util.LecteurCSV;

import java.util.List;

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
    public void initialize() {

        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colArtiste.setCellValueFactory(new PropertyValueFactory<>("artiste"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colEcoutes.setCellValueFactory(new PropertyValueFactory<>("nbr_ecoute"));

        LecteurCSV lecteur = new LecteurCSV(
                "src/main/resources/data/chansons.csv"
        );

        List<Chanson> chansons = lecteur.charger();

        tableChansons.setItems(
                FXCollections.observableArrayList(chansons)
        );
    }
}