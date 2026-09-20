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
import org.example.dao.PlaylistChansonDAO;
import org.example.dao.PlaylistChansonDAOImpl;

public class PlaylistController {

    @FXML private Label nomPlaylist;
    @FXML private Label labelDureeTotale;

    @FXML private TableView<Chanson> tablePlaylist;
    @FXML private TableColumn<Chanson, String> colTitre;
    @FXML private TableColumn<Chanson, String> colArtiste;
    @FXML private TableColumn<Chanson, Integer> colDuree;

    private Playlist playlist;
    private ServicePlaylist servicePlaylist;
    private MainController mainController;
    private PlaylistChansonDAO playlistChansonDAO = new PlaylistChansonDAOImpl();

    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colArtiste.setCellValueFactory(new PropertyValueFactory<>("artiste"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        tablePlaylist.getSelectionModel().selectedItemProperty().addListener(
                (observable, ancienneChanson, nouvelleChanson) -> {
                    if (nouvelleChanson != null && mainController != null) {
                        mainController.selectionnerChanson(nouvelleChanson);
                    }
                }
        );
    }

    public void setPlaylist(Playlist playlist, ServicePlaylist servicePlaylist) {
        this.playlist = playlist;
        this.servicePlaylist = servicePlaylist;
        nomPlaylist.setText(playlist.getNom());
        rafraichirPlaylist();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    private void rafraichirPlaylist() {
        tablePlaylist.setItems(
                FXCollections.observableArrayList(playlist.getChansons())
        );
        labelDureeTotale.setText(
                "Durée totale : " + playlist.getDureeTotale() + " s"
        );
    }
    @FXML
    private void retirerChanson() {
        Chanson chanson = tablePlaylist.getSelectionModel().getSelectedItem();
        if (chanson != null) {
            servicePlaylist.retirerChanson(playlist, chanson);
            playlistChansonDAO.supprimerChanson(
                    playlist.getId(),
                    chanson.getId()
            );
            rafraichirPlaylist();
        }
    }
    @FXML
    private void viderPlaylist() {
        for (Chanson chanson : playlist.getChansons()) {
            playlistChansonDAO.supprimerChanson(
                    playlist.getId(),
                    chanson.getId()
            );
        }

        servicePlaylist.viderPlaylist(playlist);
        rafraichirPlaylist();
    }

    @FXML
    private void monterChanson() {
        int index = tablePlaylist.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            playlist.deplacerVersLeHaut(index);
            rafraichirPlaylist();
            if (index > 0) {
                tablePlaylist.getSelectionModel().select(index - 1);
            }
        }
    }

    @FXML
    private void descendreChanson() {
        int index = tablePlaylist.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
            playlist.deplacerVersLeBas(index);
            rafraichirPlaylist();
            if (index < playlist.getChansons().size() - 1) {
                tablePlaylist.getSelectionModel().select(index + 1);
            }
        }
    }
}
