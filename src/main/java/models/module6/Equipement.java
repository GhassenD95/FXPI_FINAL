package models.module6;

import enums.EtatEquipement;
import enums.TypeEquipement;

public class Equipement {
    private int id;
    private String nom;
    private String description;
    private EtatEquipement etat;
    private TypeEquipement typeEquipement;
    private String imageUrl;  // Correction : renommé en imageUrl
    private int quantite;

    // Constructeur
    public Equipement(int id, String nom, String description, EtatEquipement etat, TypeEquipement typeEquipement, String imageUrl, int quantite) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.etat = etat;
        this.typeEquipement = typeEquipement;
        this.imageUrl = imageUrl;
        this.quantite = quantite;
    }

    // Surcharge du constructeur sans ID (pour l'ajout d'un nouvel équipement)
    public Equipement(String nom, String description, EtatEquipement etat, TypeEquipement typeEquipement, String imageUrl, int quantite) {
        this.nom = nom;
        this.description = description;
        this.etat = etat;
        this.typeEquipement = typeEquipement;
        this.imageUrl = imageUrl;
        this.quantite = quantite;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EtatEquipement getEtat() {
        return etat;
    }

    public void setEtat(EtatEquipement etat) {
        this.etat = etat;
    }

    public TypeEquipement getTypeEquipement() {
        return typeEquipement;
    }

    public void setTypeEquipement(TypeEquipement typeEquipement) {
        this.typeEquipement = typeEquipement;
    }

    public String getImageUrl() {  // Correction : getter renommé
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {  // Correction : setter renommé
        this.imageUrl = imageUrl;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "Equipement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", etat=" + etat +
                ", typeEquipement=" + typeEquipement +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantite=" + quantite +
                '}';
    }

    public String getImage_url() {
        return null;
    }

    public void setImage_url(String trim) {
        
    }
}
