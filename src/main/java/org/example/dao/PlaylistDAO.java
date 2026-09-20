package org.example.dao;

import org.example.model.Playlist;

import java.util.List;
import java.util.Optional;

public interface PlaylistDAO {

    List<Playlist> trouverTous();

    Optional<Playlist> trouverParId(int id);

    void ajouter(Playlist playlist);

    void modifier(Playlist playlist);

    void supprimer(int id);
}
