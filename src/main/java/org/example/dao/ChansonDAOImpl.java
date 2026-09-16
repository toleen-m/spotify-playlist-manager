package org.example.dao;

import org.example.model.Chanson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ChansonDAOImpl implements ChansonDAO {

    @Override
    public List<Chanson> trouverTous() {
        List<Chanson> chansons = new ArrayList<>();
        String sql = "SELECT * FROM chanson";
        try (Connection connexion = Connexion.getConnexion();
                PreparedStatement ps = connexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                Chanson chanson = new Chanson(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("artiste"),
                        rs.getString("album"),
                        rs.getInt("annee"),
                        org.example.model.Genre.valueOf(rs.getString("genre")),
                        rs.getInt("duree"),
                        rs.getInt("nbr_ecoute")
                );
                chansons.add(chanson);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des chansons : " + e.getMessage());
        }
        return chansons;
    }


}