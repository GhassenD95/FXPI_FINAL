package models.module1;

import enums.Division;
import enums.Sport;

public class Equipe {
    private int id;
    private String nom;
    private Division division;
    private Utilisateur coach;
    private Sport sport;

    public Equipe(String nom, Utilisateur coach, Division division, Sport sport) {

        this.nom = nom;
        this.coach = coach;
        this.division = division;
        this.sport = sport;
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

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Utilisateur getCoach() {
        return coach;
    }

    public void setCoach(Utilisateur coach) {
        this.coach = coach;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    @Override
    public String toString() {
        return "Equipe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", division=" + division +
                ", coach=" + coach +
                ", sport=" + sport +
                '}';
    }
}
