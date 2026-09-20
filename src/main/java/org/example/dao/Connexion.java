package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static final String URL =
            "jdbc:postgresql://localhost:5432/spotify_db";
    private static final String USER = "postgres";
    private static final String PASS = "Unaiza1231!";

    private Connexion() {}
    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}