package models.module5;

import java.util.Date;

public class MatchSportif {
    private int id;
    private int tournoisId;
    private int equipe1Id;
    private int equipe2Id;
    private Date date;
    private String lieu;

    public MatchSportif(int id, int tournoisId, int equipe1Id, int equipe2Id, Date date, String lieu) {
        this.id = id;
        this.tournoisId = tournoisId;
        this.equipe1Id = equipe1Id;
        this.equipe2Id = equipe2Id;
        this.date = date;
        this.lieu = lieu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTournoisId() {
        return tournoisId;
    }

    public void setTournoisId(int tournoisId) {
        this.tournoisId = tournoisId;
    }

    public int getEquipe1Id() {
        return equipe1Id;
    }

    public void setEquipe1Id(int equipe1Id) {
        this.equipe1Id = equipe1Id;
    }

    public int getEquipe2Id() {
        return equipe2Id;
    }

    public void setEquipe2Id(int equipe2Id) {
        this.equipe2Id = equipe2Id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    @Override
    public String toString() {
        return "MatchSportif{" +
                "id=" + id +
                ", tournoisId=" + tournoisId +
                ", equipe1Id=" + equipe1Id +
                ", equipe2Id=" + equipe2Id +
                ", date=" + date +
                ", lieu='" + lieu + '\'' +
                '}';
    }
}
