package org.example.service;

import org.example.model.Bibliotheque;
import org.example.model.Chanson;

import java.util.Random;

public class LecteurSimule {
    private Bibliotheque bibliotheque;
    private int indexActuel;
    private boolean enLecture;
    private final Random random;
    private double progression;

    public LecteurSimule(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
        this.indexActuel = 0;
        this.enLecture = false;
        this.random = new Random();
        this.progression = 0.0;
    }

    // a faire
    public double getProgression() {
        return progression;
    }

    public Chanson getChansonActuelle() {

        if (bibliotheque.getChansons().isEmpty()) {
            return null;
        }
        return bibliotheque.getChansons().get(indexActuel);
    }

    public void play() {
        enLecture = true;
    }
    public void pause() {
        enLecture = false;
    }

    public boolean estEnLecture() {
        return enLecture;
    }


    public Chanson suivant() {
        if (bibliotheque.getChansons().isEmpty()) {
            return null;
        }

        Chanson actuelle = getChansonActuelle();
        actuelle.setNbr_ecoute(actuelle.getNbr_ecoute() + 1);
        indexActuel++;

        if (indexActuel >= bibliotheque.getChansons().size()) {
            indexActuel = 0;
        }
        progression = 0.0;
        return getChansonActuelle();
    }

    public Chanson precedent() {
        if (bibliotheque.getChansons().isEmpty()) {
            return null;
        }

        indexActuel--;
        if (indexActuel < 0) {
            indexActuel = bibliotheque.getChansons().size() - 1;
        }
        progression = 0.0;
        return getChansonActuelle();
    }

    public Chanson shuffle() {

        if (bibliotheque.getChansons().isEmpty()) {
            return null;
        }
        indexActuel = random.nextInt(bibliotheque.getChansons().size());
        return getChansonActuelle();
    }

}
