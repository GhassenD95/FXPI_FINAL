package models.module5;

public class PerformanceAthlete {
    private int id;
    private int athleteId;
    private int matchId;
    private int minutesJouees;
    private int buts;
    private int passesDecisives;
    private int tirs;
    private int interceptions;
    private int fautes;
    private int cartonsJaunes;
    private int cartonsRouges;
    private int rebonds;

    public PerformanceAthlete(int id, int athleteId, int matchId, int minutesJouees, int buts, int passesDecisives, int tirs, int interceptions, int fautes, int cartonsJaunes, int cartonsRouges, int rebonds) {
        this.id = id;
        this.athleteId = athleteId;
        this.matchId = matchId;
        this.minutesJouees = minutesJouees;
        this.buts = buts;
        this.passesDecisives = passesDecisives;
        this.tirs = tirs;
        this.interceptions = interceptions;
        this.fautes = fautes;
        this.cartonsJaunes = cartonsJaunes;
        this.cartonsRouges = cartonsRouges;
        this.rebonds = rebonds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getMinutesJouees() {
        return minutesJouees;
    }

    public void setMinutesJouees(int minutesJouees) {
        this.minutesJouees = minutesJouees;
    }

    public int getButs() {
        return buts;
    }

    public void setButs(int buts) {
        this.buts = buts;
    }

    public int getPassesDecisives() {
        return passesDecisives;
    }

    public void setPassesDecisives(int passesDecisives) {
        this.passesDecisives = passesDecisives;
    }

    public int getTirs() {
        return tirs;
    }

    public void setTirs(int tirs) {
        this.tirs = tirs;
    }

    public int getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(int interceptions) {
        this.interceptions = interceptions;
    }

    public int getFautes() {
        return fautes;
    }

    public void setFautes(int fautes) {
        this.fautes = fautes;
    }

    public int getCartonsJaunes() {
        return cartonsJaunes;
    }

    public void setCartonsJaunes(int cartonsJaunes) {
        this.cartonsJaunes = cartonsJaunes;
    }

    public int getCartonsRouges() {
        return cartonsRouges;
    }

    public void setCartonsRouges(int cartonsRouges) {
        this.cartonsRouges = cartonsRouges;
    }

    public int getRebonds() {
        return rebonds;
    }

    public void setRebonds(int rebonds) {
        this.rebonds = rebonds;
    }

    @Override
    public String toString() {
        return "PerformanceAthlete{" +
                "id=" + id +
                ", athleteId=" + athleteId +
                ", matchId=" + matchId +
                ", minutesJouees=" + minutesJouees +
                ", buts=" + buts +
                ", passesDecisives=" + passesDecisives +
                ", tirs=" + tirs +
                ", interceptions=" + interceptions +
                ", fautes=" + fautes +
                ", cartonsJaunes=" + cartonsJaunes +
                ", cartonsRouges=" + cartonsRouges +
                ", rebonds=" + rebonds +
                '}';
    }
}