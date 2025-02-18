package models.module5;

import enums.Sport;
import java.util.Date;

public class Tournois {
    private int id;
    private String nom;
    private Sport sport;
    private Date dateDebut;
    private Date dateFin;
    private String adresse;

    public Tournois(String nom, Sport sport, Date dateDebut, Date dateFin, String adresse) {
        this.nom = nom;
        this.sport = sport;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.adresse = adresse;
    }


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

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
