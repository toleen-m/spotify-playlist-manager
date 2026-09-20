package org.example.dao;

import org.example.model.Chanson;
import org.example.model.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException("Erreur lors de la récupération des chansons", e);
        }
        return chansons;
    }

    @Override
    public Optional<Chanson> trouverParId(int id) {
        String sql =
                "SELECT id, titre, artiste, album, annee, genre, duree, nbr_ecoute " +
                        "FROM chanson WHERE id = ?";
        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Chanson chanson = new Chanson(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("artiste"),
                            rs.getString("album"),
                            rs.getInt("annee"),
                            Genre.valueOf(rs.getString("genre")),
                            rs.getInt("duree"),
                            rs.getInt("nbr_ecoute")
                    );
                    return Optional.of(chanson);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la chanson", e);
        }
        return Optional.empty();
    }

    @Override
    public void ajouter(Chanson chanson) {
        String sql =
                "INSERT INTO chanson " +
                        "(titre, artiste, album, annee, genre, duree, nbr_ecoute) " +
                        "VALUES (?, ?, ?, ?, ?::genre, ?, ?)";
        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setString(1, chanson.getTitre());
            statement.setString(2, chanson.getArtiste());
            statement.setString(3, chanson.getAlbum());
            statement.setInt(4, chanson.getAnnee());
            statement.setString(5, chanson.getGenre().name());
            statement.setInt(6, chanson.getDuree());
            statement.setInt(7, chanson.getNbr_ecoute());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la chanson.", e);
        }
    }

    @Override
    public void modifier(Chanson chanson) {
        String sql =
                "UPDATE chanson " +
                        "SET titre = ?, artiste = ?, album = ?, annee = ?, genre = ?::genre, duree = ?, nbr_ecoute = ? " +
                        "WHERE id = ? ";

        try (Connection connexion = Connexion.getConnexion();
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setString(1, chanson.getTitre());
            statement.setString(2, chanson.getArtiste());
            statement.setString(3, chanson.getAlbum());
            statement.setInt(4, chanson.getAnnee());
            statement.setString(5, chanson.getGenre().name());
            statement.setInt(6, chanson.getDuree());
            statement.setInt(7, chanson.getNbr_ecoute());
            statement.setInt(8, chanson.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la modification de la chanson.",e);
        }
    }


    @Override
    public void supprimer(int id) {
        String sql =
                "DELETE FROM chanson " +
                        "WHERE id = ? ";

        try (Connection connexion = Connexion.getConnexion();
                PreparedStatement statement = connexion.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la chanson.", e);
        }
    }




}