package utils;

import models.module1.Utilisateur;

public class Session {
    private Utilisateur currentUtilisateur;

    public Utilisateur getCurrentUtilisateur() {
        return currentUtilisateur;
    }

    public void setCurrentUtilisateur(Utilisateur currentUtilisateur) {
        this.currentUtilisateur = currentUtilisateur;
    }

    public void unsetCurrentUtilisateur() {
        this.currentUtilisateur = null;
    }


}
