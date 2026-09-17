package org.example;

import org.example.dao.ChansonDAO;
import org.example.dao.ChansonDAOImpl;
import org.example.dao.Connexion;
import org.example.model.Chanson;

import java.sql.Connection;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        ChansonDAO dao = new ChansonDAOImpl();

        List<Chanson> chansons = dao.trouverTous();

        for (Chanson chanson : chansons) {
            System.out.println(chanson.getTitre());
        }
    }
}