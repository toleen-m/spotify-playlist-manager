package org.example.algorithmes;

import org.example.model.Chanson;

import java.util.Comparator;
import java.util.List;

public class TriSelection implements Algorithme {

    @Override
    public String nom() {
        return "Tri par sélection";
    }

    @Override
    public String complexiteTheorique() {
        return "O(n²)";
    }

    @Override
    public void trier(List<Chanson> chansons, Comparator<Chanson> comparateur) {
        for (int i = 0; i < chansons.size() - 1; i++) {
            int indexMinimum = i;
            for (int j = i + 1; j < chansons.size(); j++) {

                if (comparateur.compare(chansons.get(j), chansons.get(indexMinimum)) < 0) {
                    indexMinimum = j;
                }
            }

            if (indexMinimum != i) {
                Chanson temporaire = chansons.get(i);
                chansons.set(i, chansons.get(indexMinimum));
                chansons.set(indexMinimum, temporaire);
            }
        }
    }
}