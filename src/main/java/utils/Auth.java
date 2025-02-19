package utils;

import models.module1.Utilisateur;
import services.BaseService;
import services.module1.ServiceUtilisateur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Auth {

    //login
    public boolean login(String email, String password) {
        try {
            Utilisateur utilisateur = new ServiceUtilisateur().getUtilisateurByEmailPassword(email, password);
            if (utilisateur != null) {
                new Session().setCurrentUtilisateur(utilisateur);
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
