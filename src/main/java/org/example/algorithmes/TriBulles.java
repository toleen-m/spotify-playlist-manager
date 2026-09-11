package org.example.algorithmes;

import org.example.model.Chanson;

import java.util.Comparator;
import java.util.List;

public class TriBulles implements Algorithme {

    @Override
    public String nom() {
        return "Tri à bulles";
    }

    @Override
    public String complexiteTheorique() {
        return "O(n²)";
    }

    @Override
    public void trier(List<Chanson> chansons, Comparator<Chanson> comparateur) {

        for (int i = 0; i < chansons.size() - 1; i++) {
            for (int j = 0; j < chansons.size() - 1 - i; j++) {

                if (comparateur.compare(chansons.get(j), chansons.get(j + 1)) > 0) {
                    Chanson temporaire = chansons.get(j);
                    chansons.set(j, chansons.get(j + 1));
                    chansons.set(j + 1, temporaire);
                }
            }
        }
    }
}