package org.example.service;

import org.example.model.Bibliotheque;
import org.example.model.Chanson;

import java.util.Random;

public class LecteurSimule {
    private Bibliotheque bibliotheque;
    private int indexActuel;
    private boolean enLecture;
    private final Random random;
    private int tempsActuel;

    public LecteurSimule(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
        this.indexActuel = 0;
        this.enLecture = false;
        this.random = new Random();
        this.tempsActuel = 0;
    }

    // a faire
    public double getProgression() {
        Chanson chanson = getChansonActuelle();
        if(chanson == null || chanson.getDuree() <= 0){
            return 0.0;
        }
        return (double) tempsActuel / chanson.getDuree();
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
        tempsActuel = 0;
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
        tempsActuel = 0;
        return getChansonActuelle();
    }

    public Chanson shuffle() {

        if (bibliotheque.getChansons().isEmpty()) {
            return null;
        }
        indexActuel = random.nextInt(bibliotheque.getChansons().size());
        tempsActuel = 0;
        return getChansonActuelle();
    }

    public Chanson avancerTemps(int secondes) {
        if (!enLecture) {
            return getChansonActuelle();
        }
        Chanson chanson = getChansonActuelle();
        if (chanson == null) {
            return null;
        }
        tempsActuel += secondes;
        if (tempsActuel >= chanson.getDuree()) {
            return suivant();
        }
        return chanson;
    }
    public void choisirChanson(Chanson chanson) {
        int index = bibliotheque.getChansons().indexOf(chanson);

        if (index != -1) {
            indexActuel = index;
            tempsActuel = 0;
        }
    }

}
