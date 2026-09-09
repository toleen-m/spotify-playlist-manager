package org.example.service;

import org.example.model.Bibliotheque;
import org.example.model.Chanson;

import java.util.ArrayList;
import java.util.List;


public class ServiceRecherche {

    private Bibliotheque bibliotheque;

    public ServiceRecherche(Bibliotheque bibliotheque){
        this.bibliotheque = bibliotheque;
    }

    public List<Chanson> rechercher(String titre, String artiste){

        List<Chanson> resultat = new ArrayList<>();

        for (Chanson chanson : bibliotheque.getChansons()){

            boolean titreCorrespond = titre == null || titre.isBlank() || chanson.getTitre().toLowerCase().contains(titre.toLowerCase());
            boolean artisteCorrespond = artiste == null || artiste.isBlank() || chanson.getArtiste().toLowerCase().contains(artiste.toLowerCase());

            if (titreCorrespond && artisteCorrespond){
                resultat.add(chanson);
            }

        }
        return resultat;
    }
}
