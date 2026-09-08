package org.example.util;

import org.example.model.Chanson;
import org.example.model.Genre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LecteurCSV implements SourceDonnees {

    private String cheminFichier;

    public LecteurCSV(String cheminFichier){

        this.cheminFichier = cheminFichier;
    }

    @Override
    public List<Chanson> charger(){
        List<Chanson> chansons = new ArrayList<>();


        try {

            BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier));

            lecteur.readLine();
            String ligne;

            while ((ligne = lecteur.readLine()) != null) {

                String[] donnees = ligne.split(",");

                int id = Integer.parseInt(donnees[0]);
                String titre = donnees[1];
                String artiste = donnees[2];
                String album = donnees[3];
                int annee = Integer.parseInt(donnees[4]);
                Genre genre = Genre.valueOf(donnees[5]);
                int duree = Integer.parseInt(donnees[6]);
                int ecoutes = Integer.parseInt(donnees[7]);

                Chanson chanson = new Chanson(
                        id,
                        titre,
                        artiste,
                        album,
                        annee,
                        genre,
                        duree,
                        ecoutes
                );

                chansons.add(chanson);
            }


        } catch (Exception e) {
            throw new RuntimeException(
                    "Erreur lors de la lecture du fichier CSV : " + cheminFichier,e);
        }

        return chansons;

    }
    
}
