package model;

public class Client {
    private int idClient;
    private String nom;
    private String email;

    public Client() {
    }

    public Client(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public Client(int idClient, String nom, String email) {
        this(nom, email);
        this.idClient = idClient;
    }

    public int getId_client() {
        return idClient;
    }

    public void setId_client(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}