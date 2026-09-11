package org.example.algorithmes;

import org.example.model.Chanson;

import java.util.Comparator;
import java.util.List;

public class TriInsertion implements Algorithme {

    @Override
    public String nom() {
        return "Tri par insertion";
    }

    @Override
    public String complexiteTheorique() {
        return "O(n²)";
    }

    @Override
    public void trier(List<Chanson> chansons, Comparator<Chanson> comparateur) {
        for (int i = 1; i < chansons.size(); i++) {
            Chanson chansonActuelle = chansons.get(i);
            int j = i - 1;
            while (j >= 0 && comparateur.compare(chansons.get(j), chansonActuelle) > 0) {
                chansons.set(j + 1, chansons.get(j));
                j--;
            }

            chansons.set(j + 1, chansonActuelle);
        }
    }
}