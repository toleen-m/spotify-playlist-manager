package org.example.dao;

import org.example.model.Playlist;

import java.util.List;

public interface PlaylistDAO {

    List<Playlist> trouverTous();

    Playlist trouverParId(int id);

    void ajouter(Playlist playlist);

    void modifier(Playlist playlist);

    void supprimer(int id);
}
