package services.module1;

import models.module1.Equipe;
import models.module1.JoueurEquipe;
import models.module1.Utilisateur;
import services.BaseService;
import services.IService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceJoueurEquipe  extends BaseService implements IService<JoueurEquipe> {

    @Override
    public void add(JoueurEquipe joueurEquipe) throws SQLException {
        String sql = "INSERT INTO utilisateurequipe(equipe_id, joueur_id) VALUES(?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, joueurEquipe.getEquipe().getId());
            ps.setInt(2, joueurEquipe.getJoueur().getId());
            ps.executeUpdate();
            System.out.println("UtilisateurEquipe has been added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(JoueurEquipe joueurEquipe) {
        String sql = "UPDATE utilisateurequipe SET equipe_id = ?, joueur_id = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, joueurEquipe.getEquipe().getId());
            ps.setInt(2, joueurEquipe.getJoueur().getId());
            ps.setInt(3, joueurEquipe.getId());
            ps.executeUpdate();
            System.out.println("JoueurEquipe has been updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(JoueurEquipe joueurEquipe) {
        String sql = "DELETE FROM utilisateurequipe WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, joueurEquipe.getId());
            ps.executeUpdate();
            System.out.println("JoueurEquipe has been deleted!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public JoueurEquipe get(int id) {
        String sql = "SELECT * FROM utilisateurequipe WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Equipe equipe = new ServiceEquipe().get(rs.getInt("equipe_id"));
                    Utilisateur joueur = new ServiceUtilisateur().get(rs.getInt("joueur_id"));
                    JoueurEquipe joueurEquipe = new JoueurEquipe(equipe, joueur);
                    joueurEquipe.setId(id);
                    return joueurEquipe;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public List<JoueurEquipe> getAll() throws SQLException {
        List<JoueurEquipe> joueurEquipes = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurequipe";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Equipe equipe = new ServiceEquipe().get(rs.getInt("equipe_id"));
                Utilisateur joueur = new ServiceUtilisateur().get(rs.getInt("joueur_id"));
                int id = rs.getInt("id");

                JoueurEquipe joueurEquipe = new JoueurEquipe(equipe, joueur);
                joueurEquipe.setId(id);
                joueurEquipes.add(joueurEquipe);
            }
        }
        return joueurEquipes;

    }
}
