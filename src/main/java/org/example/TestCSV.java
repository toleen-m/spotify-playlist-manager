package org.example;

import org.example.model.Chanson;
import org.example.util.LecteurCSV;

import java.util.List;

public class TestCSV {

    public static void main(String[] args) {

        LecteurCSV lecteur = new LecteurCSV("src/main/resources/data/chansons.csv");
        List<Chanson> chansons = lecteur.charger();

        System.out.println("Nombre de chansons : " + chansons.size());

        if (!chansons.isEmpty()) {

            Chanson premiere = chansons.get(0);

            System.out.println("Première chanson :");
            System.out.println("ID : " + premiere.getId());
            System.out.println("Titre : " + premiere.getTitre());
            System.out.println("Artiste : " + premiere.getArtiste());
            System.out.println("Album : " + premiere.getAlbum());
            System.out.println("Année : " + premiere.getAnnee());
            System.out.println("Genre : " + premiere.getGenre());
            System.out.println("Durée : " + premiere.getDuree());
            System.out.println("Écoutes : " + premiere.getNbr_ecoute());
        }
    }
}
