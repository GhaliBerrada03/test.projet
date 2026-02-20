# Art Gallery Management System

Système de gestion pour une galerie d'art développé en Java Swing avec MySQL.

## Fonctionnalités
- **Authentification** : Système de login sécurisé (admin/admin123).
- **Gestion des Œuvres** : Ajout, modification, suppression et recherche d'œuvres.
- **Support Photos** : Possibilité d'associer des photos réelles aux œuvres d'art.
- **Gestion des Clients** : Suivi des clients de la galerie.
- **Gestion des Ventes** : Enregistrement des transactions.
- **Filtrage Avancé** : Recherche par texte et filtrage des ventes par plage de dates.

## Installation
1. Cloner le projet.
2. Importer le projet dans **NetBeans 8.0.2**.
3. Ajouter `lib/mysql-connector-j-9.6.0.jar` et `lib/jcalendar-1.4.jar` aux bibliothèques du projet.
4. Importer le fichier SQL (voir dossier database) dans votre serveur MySQL local.
5. Configurer la connexion dans `src/dao/Db_connection.java`.

## Technologies
- Java JDK 8
- MySQL
- JCalendar (pour le filtrage par date)
