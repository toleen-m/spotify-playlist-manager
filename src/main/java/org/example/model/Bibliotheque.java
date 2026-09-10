package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {

    private List<Chanson> chansons;
    private List<Playlist> playlists;


    public Bibliotheque(List<Chanson> chansons){
        this.chansons = new ArrayList<>(chansons);
        this.playlists = new ArrayList<>();
    }

    public List<Chanson> getChansons() {
        return new ArrayList<>(chansons);
    }

    public List<Playlist> getPlaylists(){
        return new ArrayList<>(playlists);
    }


    public boolean ajouterChanson(Chanson chanson){
        for (Chanson c : chansons){
            if (c.getId() == chanson.getId()){
                return false;
            }
        }

        chansons.add(chanson);
        return true;
    }

    public boolean ajouterPlaylist(Playlist playlist){
        for (Playlist p : playlists){
            if(p.getId() == playlist.getId()){
                return false;
            }
        }

        playlists.add(playlist);
        return true;
    }

    public boolean supprimerPlaylist(Playlist playlist){
        return playlists.removeIf(p -> p.getId() == playlist.getId());
    }


    public Playlist getPlaylist(String nom){
        if (nom == null || nom.isEmpty()) {
            return null;
        }

        for (Playlist playlist : playlists) {
            if (playlist.getNom().equals(nom)) {
                return playlist;
            }
        }

        return null;
    }

}
