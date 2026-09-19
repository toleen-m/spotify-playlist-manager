package org.example.dao;

import java.util.List;

public interface PlaylistChansonDAO {

    void ajouterChanson(int playlistId, int chansonId);

    void supprimerChanson(int playlistId, int chansonId);

    List<Integer> trouverChansons(int playlistId);
}