package org.example.model;

public class ResultatBenchmark {

    private final String nomAlgorithme;
    private final String complexite;
    private final double tempsMillisecondes;

    public ResultatBenchmark(String nomAlgorithme, String complexite, double tempsMillisecondes) {
        this.nomAlgorithme = nomAlgorithme;
        this.complexite = complexite;
        this.tempsMillisecondes = tempsMillisecondes;
    }

    public String getNomAlgorithme() {
        return nomAlgorithme;
    }

    public String getComplexite() {
        return complexite;
    }

    public double getTempsMillisecondes() {
        return tempsMillisecondes;
    }
}