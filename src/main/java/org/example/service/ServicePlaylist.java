package org.example.service;

import org.example.model.Bibliotheque;
import org.example.model.Chanson;
import org.example.model.Playlist;

public class ServicePlaylist {

    private final Bibliotheque bibliotheque;
    private int prochainId = 1;

    public ServicePlaylist(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    public void creerPlaylist(String nom) {
        Playlist playlist = new Playlist(prochainId, nom);
        prochainId++;

        bibliotheque.ajouterPlaylist(playlist);
    }

    public void supprimerPlaylist(Playlist playlist) {

        bibliotheque.supprimerPlaylist(playlist);
    }

    public void ajouterChanson(Playlist playlist, Chanson chanson) {
        playlist.ajouterChanson(chanson);
    }

    public void retirerChanson(Playlist playlist, Chanson chanson) {
        playlist.retirerChanson(chanson);
    }

    public void monterChanson(Playlist playlist, Chanson chanson) {
        playlist.deplacerVersLeHaut(chanson.getId());
    }

    public void descendreChanson(Playlist playlist, Chanson chanson) {
        playlist.deplacerVersLeBas(chanson.getId());
    }

    public void viderPlaylist(Playlist playlist) {

        playlist.vider();
    }
}
