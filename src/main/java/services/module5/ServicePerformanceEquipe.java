package services.module5;
import enums.Division;
import enums.Sport;
import enums.Status;
import models.module1.Equipe;
import models.module1.Utilisateur;
import models.module5.PerformanceEquipe;
import models.module5.Tournois;
import services.BaseService;
import services.IService;
import services.module1.ServiceEquipe;
import enums.Role;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static enums.Role.coach;

public class ServicePerformanceEquipe extends BaseService implements IService<PerformanceEquipe> {
    @Override
    public void add(PerformanceEquipe performanceEquipe) throws SQLException {
        String sql = "INSERT INTO performanceequipe(equipe_id, tournois_id, victoires, pertes, rang) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, performanceEquipe.getEquipe().getId());
            ps.setInt(2, performanceEquipe.getTournois().getId());
            ps.setInt(3, performanceEquipe.getVictoires());
            ps.setInt(4, performanceEquipe.getPertes());
            ps.setInt(5, performanceEquipe.getRang());
            ps.executeUpdate();
            System.out.println("PerformanceEquipe has been added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(PerformanceEquipe performanceEquipe) {
        String sql = "UPDATE performanceequipe SET equipe_id = ?, tournois_id = ?, victoires = ?, pertes = ?, rang = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, performanceEquipe.getEquipe().getId());
            ps.setInt(2, performanceEquipe.getTournois().getId());
            ps.setInt(3, performanceEquipe.getVictoires());
            ps.setInt(4, performanceEquipe.getPertes());
            ps.setInt(5, performanceEquipe.getRang());
            ps.setInt(6, performanceEquipe.getId());
            ps.executeUpdate();
            System.out.println("PerformanceEquipe has been updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(PerformanceEquipe performanceEquipe) {
        String sql = "DELETE FROM performanceequipe WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, performanceEquipe.getId());
            ps.executeUpdate();
            System.out.println("PerformanceEquipe has been deleted!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public PerformanceEquipe get(int id) {
        String sql = "SELECT * FROM performanceequipe WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Equipe equipe = new ServiceEquipe().get(rs.getInt("equipe_id"));
                    Tournois tournois = new ServiceTournois().get(rs.getInt("tournois_id"));
                    int victoires = rs.getInt("victoires");
                    int pertes = rs.getInt("pertes");
                    int rang = rs.getInt("rang");

                    PerformanceEquipe performanceEquipe = new PerformanceEquipe(equipe, victoires, pertes, rang, tournois);
                    performanceEquipe.setId(id);
                    return performanceEquipe;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public List<PerformanceEquipe> getAll() throws SQLException {
        List<PerformanceEquipe> performanceEquipeList = new ArrayList<>();
        String sql = "SELECT * FROM performanceequipe";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                Equipe equipe = new ServiceEquipe().get(rs.getInt("equipe_id"));
                Tournois tournois = new ServiceTournois().get(rs.getInt("tournois_id"));
                int victoires = rs.getInt("victoires");
                int pertes = rs.getInt("pertes");
                int rang = rs.getInt("rang");

                PerformanceEquipe performanceEquipe = new PerformanceEquipe(equipe, victoires, pertes, rang, tournois);
                performanceEquipe.setId(id);
                performanceEquipeList.add(performanceEquipe);
            }
        }
        return performanceEquipeList;

    }
    public List<Tournois> getAllTournois() {
        List<Tournois> tournoisList = new ArrayList<>();
        String sql = "SELECT * FROM tournois";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                // Assuming other fields are present in the table
                Tournois tournois = new Tournois(nom, null, null, null, null);
                tournois.setId(id); // Set the ID
                tournoisList.add(tournois);
            }
            System.out.println("Tournois retrieved: " + tournoisList.size());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tournoisList;
    }
    // ServicePerformanceEquipe.java
    public List<Equipe> getAllEquipes() {
        List<Equipe> equipeList = new ArrayList<>();
        String query = "SELECT id, nom, division, coach_id, sport FROM equipe"; // Use the correct column name

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                Division division = Division.valueOf(resultSet.getString("division"));
                int coachId = resultSet.getInt("coach_id"); // Use the correct column name
                Sport sport = Sport.valueOf(resultSet.getString("sport"));

                // Fetch the coach details using the coachId
                Utilisateur coach = getCoachById(coachId);

                Equipe equipe = new Equipe(nom, coach, division, sport);
                equipeList.add(equipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipeList;
    }

    // Method to fetch coach details by ID
    private Utilisateur getCoachById(int coachId) {
        String query = "SELECT prenom, nom, role, tel, birthday, adresse, status, image_url, email, mdp_hash FROM utilisateur WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, coachId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Utilisateur(
                            resultSet.getString("prenom"),
                            resultSet.getString("nom"),
                            Role.valueOf(resultSet.getString("role")),
                            resultSet.getString("tel"),
                            resultSet.getDate("birthday"),
                            resultSet.getString("adresse"),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getString("image_url"),
                            resultSet.getString("email"),
                            resultSet.getString("mdp_hash")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
