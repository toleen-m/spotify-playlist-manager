package org.example;

import org.example.dao.PlaylistChansonDAO;
import org.example.dao.PlaylistChansonDAOImpl;
import org.example.dao.PlaylistDAO;
import org.example.dao.PlaylistDAOImpl;
import org.example.model.Playlist;

import java.util.List;

public class TestBD {

    public static void main(String[] args) {

//        PlaylistDAO playlistDAO = new PlaylistDAOImpl();
//
//        Playlist playlist = new Playlist(1, "Ma playlist");
//        playlistDAO.ajouter(playlist);


        PlaylistChansonDAO dao = new PlaylistChansonDAOImpl();
//
//        // 1. Ajouter une chanson à une playlist
//        System.out.println("=== AJOUT ===");
//
//        dao.ajouterChanson(1, 1);
//
//        System.out.println("Chanson ajoutée à la playlist.");

//        // 2. Trouver les chansons d'une playlist
//        int playlistId = 1;
//        List<Integer> chansons = dao.trouverChansons(playlistId);
//
//        System.out.println("\nIDs des chansons dans la playlist :");
//
//        for (Integer id : chansons) {
//            System.out.println("Chanson ID : " + id);
//        }


        // 4. Supprimer la relation
        System.out.println("\n=== SUPPRESSION ===");

        dao.supprimerChanson(1, 1);

        System.out.println("Chanson retirée de la playlist.");
    }
}
