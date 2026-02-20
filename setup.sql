CREATE DATABASE IF NOT EXISTS artgallerie;
USE artgallerie;

CREATE TABLE IF NOT EXISTS client (
    idClient INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS oeuvre (
    idOeuvre INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(100) NOT NULL,
    artiste VARCHAR(100) NOT NULL,
    categorie VARCHAR(50),
    prix INT NOT NULL,
    statut VARCHAR(20) DEFAULT 'disponible'
);

CREATE TABLE IF NOT EXISTS vente_art (
    idVente INT AUTO_INCREMENT PRIMARY KEY,
    idClient INT,
    idOeuvre INT,
    date_vente TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idClient) REFERENCES client(idClient),
    FOREIGN KEY (idOeuvre) REFERENCES oeuvre(idOeuvre)
);

CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin'
);

INSERT INTO utilisateur (username, password) VALUES ('admin', 'admin123');
