package org.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.model.Chanson;
import org.example.model.Playlist;
import org.example.service.ServicePlaylist;

public class PlaylistController {

    @FXML private Label nomPlaylist;
    @FXML private TableView<Chanson> tablePlaylist;
    @FXML private TableColumn<Chanson, String> colTitre;
    @FXML private TableColumn<Chanson, String> colArtiste;
    @FXML private TableColumn<Chanson, Integer> colDuree;

    private Playlist playlist;
    private ServicePlaylist servicePlaylist;

    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colArtiste.setCellValueFactory(new PropertyValueFactory<>("artiste"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
    }

    public void setPlaylist(Playlist playlist, ServicePlaylist servicePlaylist) {
        this.playlist = playlist;
        this.servicePlaylist = servicePlaylist;

        nomPlaylist.setText(playlist.getNom());

        tablePlaylist.setItems(
                FXCollections.observableArrayList(playlist.getChansons())
        );
    }

    @FXML
    private void retirerChanson() {
        Chanson chanson = tablePlaylist.getSelectionModel().getSelectedItem();

        if (chanson != null) {
            servicePlaylist.retirerChanson(playlist, chanson);

            tablePlaylist.setItems(
                    FXCollections.observableArrayList(playlist.getChansons())
            );
        }
    }
}