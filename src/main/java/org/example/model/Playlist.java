package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private int id;
    private String nom;
    private final List<Chanson> chansons;
    private final LocalDateTime dateCreation;

    public Playlist(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.chansons = new ArrayList<>();
        this.dateCreation = LocalDateTime.now();
    }

    public int getId(){
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Chanson> getChansons() {
        return new ArrayList<>(chansons);
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public int getDureeTotale(){
        int total = 0;

        for (Chanson chanson : chansons){
            total += chanson.getDuree();
        }
        return total;
    }

    public boolean ajouterChanson(Chanson chanson){
        for (Chanson c : chansons){
            if (c.getId() == chanson.getId()){
                return false;
            }
        }

        chansons.add(chanson);
        return true;
    }

    public boolean retirerChanson(Chanson chanson){
        return chansons.removeIf(c -> c.getId() == chanson.getId());
    }

    public void vider(){
        chansons.clear();
    }

    public boolean deplacerVersLeHaut(int index){
        if (index <= 0 || index >= chansons.size()){
            return false;
        }

        Chanson chanson = chansons.remove(index);
        chansons.add(index -1, chanson);
        return true;
    }

    public boolean deplacerVersLeBas(int index) {
        if (index < 0 || index >= chansons.size() - 1) {
            return false;
        }

        Chanson chanson = chansons.remove(index);
        chansons.add(index + 1, chanson);
        return true;
    }

}
