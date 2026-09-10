package org.example;

import org.example.model.Bibliotheque;
import org.example.model.Chanson;
import org.example.model.Genre;
import org.example.model.Playlist;
import org.example.service.*;
import org.example.util.LecteurCSV;

import java.util.List;

public class TestCSV {

    public static void main(String[] args) {

        LecteurCSV lecteur = new LecteurCSV("src/main/resources/data/chansons.csv");
        List<Chanson> chansons = lecteur.charger();
//
//        System.out.println("Nombre de chansons : " + chansons.size());
//
//        if (!chansons.isEmpty()) {
//
//            Chanson premiere = chansons.get(0);
//
//            System.out.println("Première chanson :");
//            System.out.println("ID : " + premiere.getId());
//            System.out.println("Titre : " + premiere.getTitre());
//            System.out.println("Artiste : " + premiere.getArtiste());
//            System.out.println("Album : " + premiere.getAlbum());
//            System.out.println("Année : " + premiere.getAnnee());
//            System.out.println("Genre : " + premiere.getGenre());
//            System.out.println("Durée : " + premiere.getDuree());
//            System.out.println("Écoutes : " + premiere.getNbr_ecoute());
//        }

        //cree la bibliotheque
        Bibliotheque bibliotheque = new Bibliotheque(chansons);
        System.out.println("Nombre de chansons : " + bibliotheque.getChansons().size());


        ServiceFiltre serviceFiltre = new ServiceFiltre(bibliotheque);
        ServiceRecherche serviceRecherche = new ServiceRecherche(bibliotheque);
        ServicePagination servicePagination = new ServicePagination();
        ServicePlaylist servicePlaylist = new ServicePlaylist(bibliotheque);


        System.out.println("\n===== TEST FILTRE =====");
        List<Chanson> filtre = serviceFiltre.filtrerChansons(
                Genre.ROCK,
                300,
                null,
                null
        );
        System.out.println("Nombre de chansons avec filtre : " + filtre.size());
        for (Chanson chanson : filtre) {
            System.out.println(chanson.getTitre() +" - "+ chanson.getArtiste() +" - "+ chanson.getGenre());
        }


        System.out.println("\n===== TEST RECHERCHE =====");
        List<Chanson> recherche = serviceRecherche.rechercher(
                "love",
                null
        );
        System.out.println("Nombre de chansons avec recherche: " + recherche.size());


        System.out.println("\n===== TEST PAGINATION =====");
        int nombrePages = servicePagination.nombrePages(
                filtre,
                25
        );
        System.out.println("Nombre de pages : " + nombrePages);



        System.out.println("\n===== TEST GESTION PLAYLIST =====");
        servicePlaylist.creerPlaylist("Mon top");

        System.out.println("Nombre de playlists : " + bibliotheque.getPlaylists().size());
        Playlist playlist = bibliotheque.getPlaylists().get(0);
        System.out.println("Playlist créée : " + playlist.getNom());


        System.out.println("\n===== AJOUT CHANSON DANS PLAYLIST =====");
        for (int i = 0; i < 5; i++) {
            servicePlaylist.ajouterChanson(
                    playlist,
                    chansons.get(i)
            );
        }
        System.out.println("Nombre de chansons dans la playlist : " + playlist.getChansons().size());


        System.out.println("\n===== TESTER DOUBLON DANS PLAYLIST =====");
        servicePlaylist.ajouterChanson(
                playlist,
                chansons.get(0)
        );

        System.out.println("Après ajout du doublon : " + playlist.getChansons().size());


        System.out.println("\n===== RETIRER CHANSON DANS PLAYLIST =====");
        servicePlaylist.retirerChanson(
                playlist,
                chansons.get(0)
        );
        System.out.println("Après suppression : " + playlist.getChansons().size());
        for (Chanson chanson : playlist.getChansons()) {
            System.out.println(chanson.getTitre() +" - "+ chanson.getArtiste() +" - "+ chanson.getGenre());
        }


        System.out.println("\n===== DUREE TOTAL DANS PLAYLIST =====");
        System.out.println("Durée totale : " + playlist.getDureeTotale());

    }
}
