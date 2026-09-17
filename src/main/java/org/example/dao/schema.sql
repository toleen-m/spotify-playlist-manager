DROP TABLE IF EXISTS playlist_chanson;
DROP TABLE IF EXISTS chanson;
DROP TABLE IF EXISTS playlist;
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

CREATE TABLE chanson (
                         id INTEGER PRIMARY KEY,
                         titre VARCHAR(200) NOT NULL,
                         artiste VARCHAR(150) NOT NULL,
                         album VARCHAR(200) NOT NULL,
                         annee INTEGER NOT NULL CHECK (annee >= 1900),
                         genre genre NOT NULL,
                         duree INTEGER NOT NULL CHECK (duree > 0),
                         nbr_ecoute INTEGER NOT NULL DEFAULT 0 CHECK (nbr_ecoute >= 0)
);

CREATE TABLE playlist (
                          id SERIAL PRIMARY KEY,
                          nom VARCHAR(150) NOT NULL,
                          date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE playlist_chanson (
                                  playlist_id INTEGER NOT NULL,
                                  chanson_id INTEGER NOT NULL,
                                  PRIMARY KEY (playlist_id, chanson_id),

                                  FOREIGN KEY (playlist_id)
                                      REFERENCES playlist(id)
                                      ON DELETE CASCADE,

                                  FOREIGN KEY (chanson_id)
                                      REFERENCES chanson(id)
                                      ON DELETE CASCADE
);