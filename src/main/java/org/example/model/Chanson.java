package org.example.model;

public class Chanson {

    private int id;
    private String titre;
    private String artist;
    private String album;
    private int annee;
    private Genre genre;
    private int duree;
    private int nbr_ecoute;

    public Chanson(int id, String titre, String artist, String album, int annee, Genre genre, int duree, int nbr_ecoute){
        this.id = id;
        this.titre = titre;
        this.artist = artist;
        this.album = album;
        this.annee = annee;
        this.genre = genre;
        this.duree = duree;
        this.nbr_ecoute = nbr_ecoute;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getAnnee() {
        return annee;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getDuree() {
        return duree;
    }

    public int getNbr_ecoute() {
        return nbr_ecoute;
    }


    public void incrementerEcoutes(){
        nbr_ecoute++;
    }
}
