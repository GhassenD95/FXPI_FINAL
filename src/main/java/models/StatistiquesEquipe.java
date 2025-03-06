package models;

public class StatistiquesEquipe {
    private int equipeId;
    private String nomEquipe;
    private int matchsJoues;
    private int victoires;
    private int nuls;
    private int defaites;
    private int butsMarques;
    private int butsConcedes;
    private int points;
    private int differenceButs;

    public StatistiquesEquipe(int equipeId, String nomEquipe) {
        this.equipeId = equipeId;
        this.nomEquipe = nomEquipe;
        this.matchsJoues = 0;
        this.victoires = 0;
        this.nuls = 0;
        this.defaites = 0;
        this.butsMarques = 0;
        this.butsConcedes = 0;
        this.points = 0;
        this.differenceButs = 0;
    }

    // Getters et Setters
    public int getEquipeId() { return equipeId; }
    public void setEquipeId(int equipeId) { this.equipeId = equipeId; }
    public String getNomEquipe() { return nomEquipe; }
    public void setNomEquipe(String nomEquipe) { this.nomEquipe = nomEquipe; }
    public int getMatchsJoues() { return matchsJoues; }
    public void setMatchsJoues(int matchsJoues) { this.matchsJoues = matchsJoues; }
    public int getVictoires() { return victoires; }
    public void setVictoires(int victoires) { this.victoires = victoires; }
    public int getNuls() { return nuls; }
    public void setNuls(int nuls) { this.nuls = nuls; }
    public int getDefaites() { return defaites; }
    public void setDefaites(int defaites) { this.defaites = defaites; }
    public int getButsMarques() { return butsMarques; }
    public void setButsMarques(int butsMarques) { this.butsMarques = butsMarques; }
    public int getButsConcedes() { return butsConcedes; }
    public void setButsConcedes(int butsConcedes) { this.butsConcedes = butsConcedes; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public int getDifferenceButs() { return differenceButs; }
    public void setDifferenceButs(int differenceButs) { this.differenceButs = differenceButs; }

    // Méthodes pour mettre à jour les statistiques
    public void ajouterMatch(int butsMarques, int butsConcedes) {
        this.matchsJoues++;
        this.butsMarques += butsMarques;
        this.butsConcedes += butsConcedes;
        this.differenceButs = this.butsMarques - this.butsConcedes;

        if (butsMarques > butsConcedes) {
            this.victoires++;
            this.points += 3;
        } else if (butsMarques == butsConcedes) {
            this.nuls++;
            this.points += 1;
        } else {
            this.defaites++;
        }
    }
} 