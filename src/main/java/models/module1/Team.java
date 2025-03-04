package models.module1;

public class Team {
    private int id;
    private String nom;
    private String division;
    private int coachId;  // references users.id
    private String sport;

    public Team(int id, String nom, String division, int coachId, String sport) {
        this.id = id;
        this.nom = nom;
        this.division = division;
        this.coachId = coachId;
        this.sport = sport;
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public int getCoachId() { return coachId; }
    public void setCoachId(int coachId) { this.coachId = coachId; }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }
}
