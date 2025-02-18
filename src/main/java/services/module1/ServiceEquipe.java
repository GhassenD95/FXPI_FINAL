package services.module1;

import enums.Division;
import enums.Sport;
import models.module1.Equipe;
import models.module1.Utilisateur;
import services.BaseService;
import services.IService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceEquipe extends BaseService implements IService<Equipe> {
    @Override
    public void add(Equipe equipe) throws SQLException{
        String sql = "INSERT INTO equipe(nom, division, coach_id, sport) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, equipe.getNom());
            ps.setString(2, equipe.getDivision().getDivisionName());
            ps.setInt(3, equipe.getCoach());
            ps.setString(4, equipe.getSport().name());
            ps.executeUpdate();

            System.out.println("Equipe a ete ajoute!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(Equipe equipe) {
        String sql = "UPDATE equipe SET nom = ?, division = ?, coach_id = ?, sport = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, equipe.getNom());
            ps.setString(2, equipe.getDivision().name());
            ps.setInt(3, equipe.getCoach());
            ps.setString(4, equipe.getSport().name());
            ps.setInt(5, equipe.getId());
            ps.executeUpdate();
            System.out.println("Equipe has been updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(Equipe equipe) {
        String sql = "DELETE FROM equipe WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, equipe.getId());
            ps.executeUpdate();
            System.out.println("Equipe has been deleted!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Equipe get(int id) {
        String sql = "SELECT * FROM equipe WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nom");
                    Division division = Division.valueOf(rs.getString("division"));
                    Utilisateur coach = new ServiceUtilisateur().get(rs.getInt("coach_id"));
                    Sport sport = Sport.valueOf(rs.getString("sport"));
                    Equipe equipe = new Equipe(nom, coach.getId(), division, sport);
                    equipe.setId(id);
                    return equipe;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Equipe> getAll() throws SQLException {
        List<Equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipe";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nom = rs.getString("nom");
                Division division = Division.valueOf(rs.getString("division"));
                Utilisateur coach = new ServiceUtilisateur().get(rs.getInt("coach_id"));
                Sport sport = Sport.valueOf(rs.getString("sport"));
                int id = rs.getInt("id");

                Equipe equipe = new Equipe(nom, coach.getId(),division, sport);
                equipe.setId(id);
                equipes.add(equipe);
            }
        }
        return equipes;

    }
}
