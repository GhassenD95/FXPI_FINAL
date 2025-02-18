package models.module1;

public class JoueurEquipe {
    private int id;
    private Equipe equipe;
    private Utilisateur joueur;

    public JoueurEquipe(Equipe equipe, Utilisateur joueur) {
        this.equipe = equipe;
        this.joueur = joueur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Utilisateur getJoueur() {
        return joueur;
    }

    public void setJoueur(Utilisateur joueur) {
        this.joueur = joueur;
    }
}
