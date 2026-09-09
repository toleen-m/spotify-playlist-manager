package org.example.service;

import org.example.model.Chanson;

import java.util.List;

public class ServicePagination {

    public int nombrePages(List<Chanson> chansons, int taillePage) {

        if (taillePage < 1 || chansons.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil(
                (double) chansons.size() / taillePage
        );
    }
}
