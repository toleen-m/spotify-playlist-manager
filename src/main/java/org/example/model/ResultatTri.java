package org.example.model;

import java.util.List;

public class ResultatTri {

    private String nomAlgorithme;
    private String complexite;
    private List<Chanson> chansons;
    private double tempsMillisecondes;

    public ResultatTri(String nomAlgorithme, String complexite, List<Chanson> chansons, double tempsMillisecondes) {
        this.nomAlgorithme = nomAlgorithme;
        this.complexite = complexite;
        this.chansons = chansons;
        this.tempsMillisecondes = tempsMillisecondes;
    }

    public String getNomAlgorithme() {
        return nomAlgorithme;
    }

    public String getComplexite() {
        return complexite;
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public double getTempsMillisecondes() {
        return tempsMillisecondes;
    }
}