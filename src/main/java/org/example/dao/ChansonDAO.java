package org.example.dao;

import org.example.model.Chanson;

import java.util.List;
import java.util.Optional;

public interface ChansonDAO {

    List<Chanson> trouverTous();

    Optional<Chanson> trouverParId(int id);

    void ajouter(Chanson chanson);

    void modifier(Chanson chanson);

    void supprimer(int id);
}