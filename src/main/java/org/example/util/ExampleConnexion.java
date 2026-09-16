package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExampleConnexion {

    private static final String URL = "jdbc:postgresql://localhost:5432/spotify_db";
    private static final String USER = "postgres";
    private static final String PASS = "votre_mot_de_passe";

    private ExampleConnexion(){};

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASS);
    }
}
