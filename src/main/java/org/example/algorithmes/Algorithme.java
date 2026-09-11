package org.example.algorithmes;

import org.example.model.Chanson;

import java.util.Comparator;
import java.util.List;

public interface Algorithme {

    String nom();
    String complexiteTheorique();

    void trier(List<Chanson> chansons, Comparator<Chanson> comparateur);
}
