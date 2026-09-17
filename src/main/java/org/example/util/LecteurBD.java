package org.example.util;

import org.example.dao.ChansonDAO;
import org.example.dao.ChansonDAOImpl;
import org.example.model.Chanson;

import java.util.List;

public class LecteurBD implements SourceDonnees {

    private final ChansonDAO chansonDAO;

    public LecteurBD() {
        chansonDAO = new ChansonDAOImpl();
    }

    @Override
    public List<Chanson> charger() {
        return chansonDAO.trouverTous();
    }
}