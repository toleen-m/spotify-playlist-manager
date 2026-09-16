DROP TABLE IF EXISTS playlist_chanson;
DROP TABLE IF EXISTS chanson;
DROP TABLE IF EXISTS playlist;
DROP TABLE IF EXISTS bibliotheque;
DROP TYPE IF EXISTS genre;


CREATE TYPE genre AS ENUM (
    'POP',
    'ROCK',
    'HIPHOP',
    'JAZZ',
    'CLASSIQUE',
    'ELECTRONIC',
    'METAL',
    'COUNTRY',
    'RNB',
    'REGGAE'
);

CREATE TABLE bibliotheque (
                          id SERIAL PRIMARY KEY,
                          nom VARCHAR(150) NOT NULL UNIQUE
);



CREATE TABLE playlist (
                      id SERIAL PRIMARY KEY,
                      nom VARCHAR(150) NOT NULL,
                      date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      bibliotheque_id INTEGER NOT NULL,

                      CONSTRAINT fk_playlist_bibliotheque
                          FOREIGN KEY (bibliotheque_id)
                              REFERENCES bibliotheque(id)
                              ON DELETE CASCADE
);



CREATE TABLE chanson (
                     id SERIAL PRIMARY KEY,
                     titre VARCHAR(200) NOT NULL,
                     artiste VARCHAR(150) NOT NULL,
                     album VARCHAR(200) NOT NULL,
                     annee INTEGER NOT NULL CHECK ( annee >= 1900 ),
                     genre genre NOT NULL,
                     duree INTEGER NOT NULL CHECK ( duree > 0 ),
                     nbr_ecoute INTEGER NOT NULL DEFAULT 0 CHECK ( nbr_ecoute >= 0 ),
                     bibliotheque_id INTEGER NOT NULL,


                     CONSTRAINT fk_chanson_bibliotheque
                         FOREIGN KEY (bibliotheque_id)
                             REFERENCES bibliotheque(id)
                             ON DELETE CASCADE
);


CREATE TABLE playlist_chanson (
                              playlist_id INTEGER NOT NULL,
                              chanson_id INTEGER NOT NULL,
                              PRIMARY KEY (playlist_id, chanson_id),

                              CONSTRAINT fk_playlist_chanson_playlist
                                  FOREIGN KEY (playlist_id)
                                      REFERENCES playlist(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_playlist_chanson_chanson
                                  FOREIGN KEY (chanson_id)
                                      REFERENCES chanson(id)
                                      ON DELETE CASCADE
);