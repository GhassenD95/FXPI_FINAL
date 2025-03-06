package models;

import java.sql.Timestamp;

public class MatchSportif {
    private int id;
    private int tournoisId;
    private int equipe1Id;
    private int equipe2Id;
    private Timestamp timestamp;
    private String lieu;
    private String tournoi;
    private String equipe1;
    private String equipe2;

    public MatchSportif() {
    }

    public MatchSportif(int id, int tournoisId, int equipe1Id, int equipe2Id, Timestamp timestamp, String lieu) {
        this.id = id;
        this.tournoisId = tournoisId;
        this.equipe1Id = equipe1Id;
        this.equipe2Id = equipe2Id;
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getTournoi() {
        return tournoi;
    }

    public void setTournoi(String tournoi) {
        this.tournoi = tournoi;
    }

    public String getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(String equipe1) {
        this.equipe1 = equipe1;
    }

    public String getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(String equipe2) {
        this.equipe2 = equipe2;
    }

    @Override
    public String toString() {
        return "MatchSportif{" +
                "id=" + id +
                ", tournoisId=" + tournoisId +
                ", equipe1Id=" + equipe1Id +
                ", equipe2Id=" + equipe2Id +
                ", timestamp=" + timestamp +
                ", lieu='" + lieu + '\'' +
                '}';
    }
}
