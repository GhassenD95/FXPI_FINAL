package models;

public class StatistiquesAthlete {
    private int athleteId;
    private String nomAthlete;
    private int matchsJoues;
    private int minutesTotales;
    private int buts;
    private int passesDecisives;
    private int tirs;
    private int interceptions;
    private int fautes;
    private int cartonsJaunes;
    private int cartonsRouges;
    private int rebonds;
    private double moyenneButs;
    private double moyennePasses;
    private double efficacite;
    private double moyenneMinutes;

    public StatistiquesAthlete(int athleteId, String nomAthlete) {
        this.athleteId = athleteId;
        this.nomAthlete = nomAthlete;
        this.matchsJoues = 0;
        this.minutesTotales = 0;
        this.buts = 0;
        this.passesDecisives = 0;
        this.tirs = 0;
        this.interceptions = 0;
        this.fautes = 0;
        this.cartonsJaunes = 0;
        this.cartonsRouges = 0;
        this.rebonds = 0;
        this.moyenneButs = 0;
        this.moyennePasses = 0;
        this.efficacite = 0;
        this.moyenneMinutes = 0;
    }

    // Getters et Setters
    public int getAthleteId() { return athleteId; }
    public void setAthleteId(int athleteId) { this.athleteId = athleteId; }
    public String getNomAthlete() { return nomAthlete; }
    public void setNomAthlete(String nomAthlete) { this.nomAthlete = nomAthlete; }
    public int getMatchsJoues() { return matchsJoues; }
    public void setMatchsJoues(int matchsJoues) { this.matchsJoues = matchsJoues; }
    public int getMinutesTotales() { return minutesTotales; }
    public void setMinutesTotales(int minutesTotales) { this.minutesTotales = minutesTotales; }
    public int getButs() { return buts; }
    public void setButs(int buts) { this.buts = buts; }
    public int getPassesDecisives() { return passesDecisives; }
    public void setPassesDecisives(int passesDecisives) { this.passesDecisives = passesDecisives; }
    public int getTirs() { return tirs; }
    public void setTirs(int tirs) { this.tirs = tirs; }
    public int getInterceptions() { return interceptions; }
    public void setInterceptions(int interceptions) { this.interceptions = interceptions; }
    public int getFautes() { return fautes; }
    public void setFautes(int fautes) { this.fautes = fautes; }
    public int getCartonsJaunes() { return cartonsJaunes; }
    public void setCartonsJaunes(int cartonsJaunes) { this.cartonsJaunes = cartonsJaunes; }
    public int getCartonsRouges() { return cartonsRouges; }
    public void setCartonsRouges(int cartonsRouges) { this.cartonsRouges = cartonsRouges; }
    public int getRebonds() { return rebonds; }
    public void setRebonds(int rebonds) { this.rebonds = rebonds; }
    public double getMoyenneButs() { return moyenneButs; }
    public void setMoyenneButs(double moyenneButs) { this.moyenneButs = moyenneButs; }
    public double getMoyennePasses() { return moyennePasses; }
    public void setMoyennePasses(double moyennePasses) { this.moyennePasses = moyennePasses; }
    public double getEfficacite() { return efficacite; }
    public void setEfficacite(double efficacite) { this.efficacite = efficacite; }
    public double getMoyenneMinutes() { return moyenneMinutes; }
    public void setMoyenneMinutes(double moyenneMinutes) { this.moyenneMinutes = moyenneMinutes; }

    // Méthode pour ajouter une performance
    public void ajouterPerformance(int minutes, int buts, int passes, int tirs, 
                                 int interceptions, int fautes, int jaunes, int rouges, int rebonds) {
        this.matchsJoues++;
        this.minutesTotales += minutes;
        this.buts += buts;
        this.passesDecisives += passes;
        this.tirs += tirs;
        this.interceptions += interceptions;
        this.fautes += fautes;
        this.cartonsJaunes += jaunes;
        this.cartonsRouges += rouges;
        this.rebonds += rebonds;

        // Calcul des moyennes
        this.moyenneButs = (double) this.buts / this.matchsJoues;
        this.moyennePasses = (double) this.passesDecisives / this.matchsJoues;
        this.moyenneMinutes = (double) this.minutesTotales / this.matchsJoues;
        
        // Calcul de l'efficacité (buts/tirs)
        if (this.tirs > 0) {
            this.efficacite = (double) this.buts / this.tirs;
        }
    }
} 