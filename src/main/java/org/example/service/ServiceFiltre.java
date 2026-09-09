package org.example.service;

import org.example.model.Bibliotheque;
import org.example.model.Chanson;
import org.example.model.Genre;
import org.example.model.Playlist;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceFiltre {

    private Bibliotheque bibliotheque;

    public ServiceFiltre(Bibliotheque bibliotheque){
        this.bibliotheque = bibliotheque;
    }

    public List<Chanson> filtrerChansons(Genre genre, Integer dureeMax, Integer nbrEcouteMin, Playlist playlist){


        return bibliotheque.getChansons().stream()
                .filter(chanson -> genre == null || chanson.getGenre() == genre)
                .filter(chanson -> dureeMax == null || chanson.getDuree() <= dureeMax)
                .filter(chanson -> nbrEcouteMin == null || chanson.getNbr_ecoute() >= nbrEcouteMin)
                .filter(chanson -> playlist == null || playlist.contient(chanson))
                .collect(Collectors.toList());


    }

}
