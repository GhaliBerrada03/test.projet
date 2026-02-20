package model;

public class Oeuvre {
    private int idOeuvre;
    private String titre;
    private String artiste;
    private String categorie;
    private double prix;
    private String statut;
    private String imagePath;

    public Oeuvre() {
    }

    public Oeuvre(String titre, String artiste, String categorie, double prix) {
        this.titre = titre;
        this.artiste = artiste;
        this.categorie = categorie;
        this.prix = prix;
    }

    public Oeuvre(String titre, String artiste, String categorie, double prix, String imagePath) {
        this(titre, artiste, categorie, prix);
        this.imagePath = imagePath;
    }

    public Oeuvre(String titre, String artiste, String categorie, double prix, int idOeuvre) {
        this(titre, artiste, categorie, prix);
        this.idOeuvre = idOeuvre;
    }

    public Oeuvre(String titre, String artiste, String categorie, double prix, int idOeuvre, String imagePath) {
        this(titre, artiste, categorie, prix, idOeuvre);
        this.imagePath = imagePath;
    }

    public int getIdOeuvre() {
        return idOeuvre;
    }

    public void setIdOeuvre(int idOeuvre) {
        this.idOeuvre = idOeuvre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}