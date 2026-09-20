package org.example;

import org.example.dao.PlaylistDAO;
import org.example.dao.PlaylistDAOImpl;
import org.example.model.Playlist;

public class TestBD {

    public static void main(String[] args) {

        PlaylistDAO dao = new PlaylistDAOImpl();

        int idTest = 100;

        // =========================
        // AJOUT
        // =========================
        System.out.println("=== AJOUT ===");

        Playlist playlist = new Playlist(
                idTest,
                "Playlist Test"
        );

        dao.ajouter(playlist);

        System.out.println("Playlist ajoutée avec succès !");


        // =========================
        // RECHERCHE PAR ID
        // =========================
        System.out.println("\n=== RECHERCHE PAR ID ===");

        Playlist resultat = dao.trouverParId(idTest);

        System.out.println(resultat);


        // =========================
        // MODIFICATION
        // =========================
        System.out.println("\n=== MODIFICATION ===");

        playlist.setNom("Playlist Modifiée");

        dao.modifier(playlist);

        System.out.println("Playlist modifiée avec succès !");

        Playlist apresModification = dao.trouverParId(idTest);

        System.out.println(apresModification);


        // =========================
        // SUPPRESSION
        // =========================
        System.out.println("\n=== SUPPRESSION ===");

        dao.supprimer(idTest);

        System.out.println("Playlist supprimée avec succès !");


        // =========================
        // VÉRIFICATION
        // =========================
        System.out.println("\n=== VÉRIFICATION ===");

        Playlist apresSuppression = dao.trouverParId(idTest);

        System.out.println(apresSuppression);
    }
}