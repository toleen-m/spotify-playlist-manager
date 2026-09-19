package org.example.dao;

import org.example.dao.Connexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistChansonDAOImpl implements PlaylistChansonDAO {

    @Override
    public void ajouterChanson(int playlistId, int chansonId) {
        String sql =
                "INSERT INTO playlist_chanson (playlist_id, chanson_id) " +
                        "VALUES (?, ?)";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, chansonId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la chanson dans la playlist", e);
        }
    }

    @Override
    public void supprimerChanson(int playlistId, int chansonId) {
        String sql =
                "DELETE FROM playlist_chanson " +
                        "WHERE playlist_id = ? AND chanson_id = ?";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, chansonId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la chanson de la playlist", e);
        }
    }

    @Override
    public List<Integer> trouverChansons(int playlistId) {
        List<Integer> chansons = new ArrayList<>();
        String sql =
                "SELECT chanson_id " +
                        "FROM playlist_chanson " +
                        "WHERE playlist_id = ?";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    chansons.add(rs.getInt("chanson_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des chansons de la playlist", e);
        }
        return chansons;
    }
}