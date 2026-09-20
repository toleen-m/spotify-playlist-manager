package org.example.dao;

import org.example.model.Playlist;
import org.example.dao.Connexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaylistDAOImpl implements PlaylistDAO {

    @Override
    public List<Playlist> trouverTous() {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlist";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Playlist playlist = new Playlist(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des playlists", e);}
        return playlists;
    }

    @Override
    public Optional<Playlist> trouverParId(int id) {
        String sql =
                "SELECT id, nom " +
                        "FROM playlist " +
                        "WHERE id = ?";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Playlist playlist = new Playlist(
                            rs.getInt("id"),
                            rs.getString("nom")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la playlist", e);
        }
        return Optional.empty();
    }

    @Override
    public void ajouter(Playlist playlist) {
        String sql =
                "INSERT INTO playlist (id, nom) " +
                        "VALUES (?, ?)";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, playlist.getId());
            ps.setString(2, playlist.getNom());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur lors de l'ajout de la playlist", e);
        }
    }

    @Override
    public void modifier(Playlist playlist) {
        String sql =
                "UPDATE playlist " +
                        "SET nom = ? " +
                        "WHERE id = ?";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setString(1, playlist.getNom());
            ps.setInt(2, playlist.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur lors de la modification de la playlist", e);
        }
    }

    @Override
    public void supprimer(int id) {
        String sql =
                "DELETE FROM playlist " +
                        "WHERE id = ?";

        try (Connection connexion = Connexion.getConnexion();
             PreparedStatement ps = connexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la playlist", e);
        }
    }
}