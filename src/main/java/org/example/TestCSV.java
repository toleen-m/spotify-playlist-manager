package org.example;

import org.example.model.Bibliotheque;
import org.example.model.Chanson;
import org.example.model.Genre;
import org.example.service.ServiceFiltre;
import org.example.service.ServiceRecherche;
import org.example.util.LecteurCSV;

import java.util.List;

public class TestCSV {

    public static void main(String[] args) {

        LecteurCSV lecteur = new LecteurCSV("src/main/resources/data/chansons.csv");
        List<Chanson> chansons = lecteur.charger();
//
//        System.out.println("Nombre de chansons : " + chansons.size());
//
//        if (!chansons.isEmpty()) {
//
//            Chanson premiere = chansons.get(0);
//
//            System.out.println("Première chanson :");
//            System.out.println("ID : " + premiere.getId());
//            System.out.println("Titre : " + premiere.getTitre());
//            System.out.println("Artiste : " + premiere.getArtiste());
//            System.out.println("Album : " + premiere.getAlbum());
//            System.out.println("Année : " + premiere.getAnnee());
//            System.out.println("Genre : " + premiere.getGenre());
//            System.out.println("Durée : " + premiere.getDuree());
//            System.out.println("Écoutes : " + premiere.getNbr_ecoute());
//        }

        //cree la bibliotheque
        Bibliotheque bibliotheque = new Bibliotheque(chansons);

        //cree les servics
        ServiceFiltre serviceFiltre = new ServiceFiltre(bibliotheque);
        ServiceRecherche serviceRecherche = new ServiceRecherche(bibliotheque);


        System.out.println("Nombre de chansons : " + bibliotheque.getChansons().size());


        //test1
        System.out.println("\n===== TEST GENRE ROCK =====");
        List<Chanson> rock = serviceFiltre.filtrerChansons(
                Genre.ROCK,
                null,
                null,
                null
        );

        System.out.println("Nombre de chansons ROCK : "+ rock.size());

        for (Chanson chanson : rock) {
            System.out.println(chanson.getTitre() +" - " + chanson.getArtiste() +" - "+ chanson.getGenre());
        }

        //test2
        System.out.println("\n===== TEST DURÉE MAX =====");

        List<Chanson> chansonsCourtes = serviceFiltre.filtrerChansons(
                null,
                200,
                null,
                null
        );

        System.out.println("======Chansons de 200 secondes ou moins : "+ chansonsCourtes.size());
        for (Chanson chanson : chansonsCourtes) {
            System.out.println(chanson.getTitre() +" - "+ chanson.getArtiste() +" - "+ chanson.getGenre());
        }

        //test3
        System.out.println("\n===== TEST ÉCOUTES MINIMUM =====");

        List<Chanson> populaires = serviceFiltre.filtrerChansons(
                null,
                null,
                40000,
                null
        );

        System.out.println("Chansons avec au moins 10000 écoutes : "+ populaires.size());
        for (Chanson chanson : populaires) {
            System.out.println(chanson.getTitre() +" - "+ chanson.getArtiste() +" - "+ chanson.getGenre());
        }




        //test4
        System.out.println("\n===== TEST FILTRES COMBINÉS =====");

        List<Chanson> resultat = serviceFiltre.filtrerChansons(
                Genre.ROCK,
                240,
                0,
                null
        );

        System.out.println("filtes combines: "+ resultat.size());
        for (Chanson chanson : resultat) {
            System.out.println(chanson.getTitre() +" - "+ chanson.getArtiste() +" - "+ chanson.getGenre());
        }



        //test5
        System.out.println("\n===== TITRE LOVE =====");

        List<Chanson> love = serviceRecherche.rechercher("love", null);

        System.out.println("Nombre : " + love.size());
        for (Chanson chanson : love) {
            System.out.println(chanson.getTitre() + " - "+ chanson.getArtiste());
        }


        //test6
        System.out.println("\n===== ARTISTE BEATLES =====");

        List<Chanson> beatles = serviceRecherche.rechercher(null, "beatles");

        System.out.println("Nombre : " + beatles.size());
        for (Chanson chanson : beatles) {
            System.out.println(chanson.getTitre() + " - "+ chanson.getArtiste());
        }


        //test7
        System.out.println("\n===== LOVE ET BEATLES =====");

        List<Chanson> loveBeatles = serviceRecherche.rechercher("love", "beatles");

        System.out.println("Nombre : " + loveBeatles.size());
        for (Chanson chanson : loveBeatles) {
            System.out.println(chanson.getTitre() + " - "+ chanson.getArtiste()
            );
        }




    }
}
