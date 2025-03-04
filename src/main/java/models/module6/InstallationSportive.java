package models.module6;

import models.module6.Equipement;
import enums.TypeInstallation;

import java.util.ArrayList;
import java.util.List;

public class InstallationSportive {
    private int id;
    private String nom;
    private TypeInstallation typeInstallation;
    private String adresse;
    private int capacite;
    private boolean isDisponible;
    private String image_url;

    private List<Equipement> equipements;

    public InstallationSportive(int id, String nom, TypeInstallation typeInstallation, String adresse, int capacite, boolean isDisponible, String image_url) {
        this.id = id;
        this.nom = nom;
        this.typeInstallation = typeInstallation;
        this.adresse = adresse;
        this.capacite = capacite;
        this.isDisponible = isDisponible;
        this.image_url = image_url;
        this.equipements = new ArrayList<>();
    }

    public InstallationSportive(String nom, TypeInstallation typeInstallation, String adresse, int capacite, boolean isDisponible, String image_url) {
        this.nom = nom;
        this.typeInstallation = typeInstallation;
        this.adresse = adresse;
        this.capacite = capacite;
        this.isDisponible = isDisponible;
        this.image_url = image_url;
        this.equipements = new ArrayList<>();
    }

    public InstallationSportive() {}

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

    public TypeInstallation getTypeInstallation() {
        return typeInstallation;
    }

    public void setTypeInstallation(TypeInstallation typeInstallation) {
        this.typeInstallation = typeInstallation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public boolean isDisponible() {
        return isDisponible;
    }

    public void setDisponible(boolean disponible) {
        isDisponible = disponible;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
