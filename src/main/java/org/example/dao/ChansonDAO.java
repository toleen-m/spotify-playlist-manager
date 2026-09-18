package org.example.dao;

import org.example.model.Chanson;

import java.util.List;

public interface ChansonDAO {

    List<Chanson> trouverTous();

    Chanson trouverParId(int id);

    void ajouter(Chanson chanson);

    void modifier(Chanson chanson);

    void supprimer(int id);


}