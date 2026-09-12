package org.example.service;

import org.example.model.ResultatBenchmark;
import org.example.model.ResultatTri;

import java.util.ArrayList;
import java.util.List;

public class ServiceBenchmark {

    private final ServiceTri serviceTri;

    public ServiceBenchmark() {
        serviceTri = new ServiceTri();
    }


    public List<ResultatBenchmark> obtenirResultatsBenchmark(List<ResultatTri> resultatsTri) {

        List<ResultatBenchmark> resultats = new ArrayList<>();
        for (ResultatTri resultatTri : resultatsTri) {
            ResultatBenchmark resultat = new ResultatBenchmark(
                    resultatTri.getNomAlgorithme(),
                    resultatTri.getComplexite(),
                    resultatTri.getTempsMillisecondes()
            );
            resultats.add(resultat);
        }

        return resultats;
    }
}