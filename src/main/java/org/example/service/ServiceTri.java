package org.example.service;

import org.example.algorithmes.Algorithme;
import org.example.algorithmes.TriBulles;
import org.example.algorithmes.TriInsertion;
import org.example.algorithmes.TriSelection;
import org.example.model.Chanson;
import org.example.model.ResultatTri;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceTri {

    private final List<Algorithme> algorithmes;

    public ServiceTri() {
        algorithmes = new ArrayList<>();
        algorithmes.add(new TriBulles());
        algorithmes.add(new TriInsertion());
        algorithmes.add(new TriSelection());
    }

    public List<Algorithme> getAlgorithmes() {
        return new ArrayList<>(algorithmes);
    }

    public List<ResultatTri> trierAvecLesTroisAlgorithmes(List<Chanson> chansons, Comparator<Chanson> comparateur) {
        List<ResultatTri> resultats = new ArrayList<>();
        for (Algorithme algorithme : algorithmes) {
            List<Chanson> copie = new ArrayList<>(chansons);
            long debut = System.nanoTime();
            algorithme.trier(copie, comparateur);
            long fin = System.nanoTime();
            double tempsMillisecondes = (fin - debut) / 1_000_000.0;
            ResultatTri resultat = new ResultatTri(
                    algorithme.nom(),
                    algorithme.complexiteTheorique(),
                    copie,
                    tempsMillisecondes
            );

            resultats.add(resultat);
        }

        return resultats;
    }

    public ResultatTri trouverLePlusRapide(List<ResultatTri> resultats) {

        if (resultats.isEmpty()) {
            return null;
        }
        ResultatTri gagnant = resultats.get(0);
        for (ResultatTri resultat : resultats) {
            if (resultat.getTempsMillisecondes() < gagnant.getTempsMillisecondes()) {
                gagnant = resultat;
            }
        }
        return gagnant;
    }



    public Comparator<Chanson> parTitre() {
        return Comparator.comparing(Chanson::getTitre);
    }

    public Comparator<Chanson> parArtiste() {
        return Comparator.comparing(Chanson::getArtiste);
    }

    public Comparator<Chanson> parDuree() {
        return Comparator.comparingInt(Chanson::getDuree);
    }

    public Comparator<Chanson> parAnnee() {
        return Comparator.comparingInt(Chanson::getAnnee).reversed();
    }

    public Comparator<Chanson> parEcoutes() {
        return Comparator.comparingInt(Chanson::getNbr_ecoute).reversed();
    }

    public Comparator<Chanson> parGenre() {
        return Comparator.comparing(chanson -> chanson.getGenre().name());
    }
}