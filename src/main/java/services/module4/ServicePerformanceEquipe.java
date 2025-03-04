
package services.module4;

import models.module1.Equipe;
import models.module1.Team;
import models.module4.PerformanceEquipe;
import models.module4.Tournois;
import services.BaseService;
import services.IService1;
import services.module1.TeamService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicePerformanceEquipe extends BaseService implements IService1<PerformanceEquipe> {

    @Override
    public void add(PerformanceEquipe performanceEquipe) throws SQLException {
        String sql = "INSERT INTO performanceequipe(equipe_id, tournois_id, victoires, pertes, rang) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, performanceEquipe.getEquipe().getId());
            stmt.setInt(2, performanceEquipe.getTournois().getId());
            stmt.setInt(3, performanceEquipe.getVictoires());
            stmt.setInt(4, performanceEquipe.getPertes());
            stmt.setInt(5, performanceEquipe.getRang());
            stmt.executeUpdate();
            System.out.println("Performance equipe ajoutée");
        }
    }

    @Override
    public void delete(PerformanceEquipe performance) throws SQLException {
        String query = "DELETE FROM performanceequipe WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, performance.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public PerformanceEquipe get(int id) throws SQLException {
        String sql = "SELECT * FROM performanceequipe WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new PerformanceEquipe(
                            rs.getInt("id"),
                            new TeamService(con).getTeamById(rs.getInt("equipe_id")),
                            new ServiceTournois().get(rs.getInt("tournois_id")),
                            rs.getInt("victoires"),
                            rs.getInt("pertes"),
                            rs.getInt("rang")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<PerformanceEquipe> getAll() throws SQLException {
        List<PerformanceEquipe> performanceEquipes = new ArrayList<>();
        String sql = "SELECT * FROM performanceequipe";
        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                performanceEquipes.add(new PerformanceEquipe(
                        rs.getInt("id"),
                        new TeamService(con).getTeamById(rs.getInt("equipe_id")),
                        new ServiceTournois().get(rs.getInt("tournois_id")),
                        rs.getInt("victoires"),
                        rs.getInt("pertes"),
                        rs.getInt("rang")
                ));
            }
        }
        return performanceEquipes;
    }

    @Override
    public void update(PerformanceEquipe performanceEquipe) throws SQLException {
        String sql = "UPDATE performanceequipe SET equipe_id = ?, tournois_id = ?, victoires = ?, pertes = ?, rang = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, performanceEquipe.getEquipe().getId());
            ps.setInt(2, performanceEquipe.getTournois().getId());
            ps.setInt(3, performanceEquipe.getVictoires());
            ps.setInt(4, performanceEquipe.getPertes());
            ps.setInt(5, performanceEquipe.getRang());
            ps.setInt(6, performanceEquipe.getId());
            ps.executeUpdate();
            System.out.println("PerformanceEquipe a été mise à jour !");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour de PerformanceEquipe: " + e.getMessage());
            throw e;
        }
    }

    public List<Team> getAllEquipes() throws SQLException {
        List<Team> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipe";
        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Team equipe = new Team(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("division"),
                        rs.getInt("coach_id"),
                        rs.getString("sport")
                );                equipe.setId(rs.getInt("id"));
                equipe.setNom(rs.getString("nom"));
                equipes.add(equipe);
            }
        }
        return equipes;
    }

    public List<Tournois> getAllTournois() {
        List<Tournois> tournoisList = new ArrayList<>();
        String sql = "SELECT * FROM tournois";
        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Tournois tournois = new Tournois();
                tournois.setId(rs.getInt("id"));
                tournois.setNom(rs.getString("nom"));
                tournoisList.add(tournois);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des Tournois : " + e.getMessage());
        }
        return tournoisList;
    }

    // Assuming you are dealing with a single Team object
    public List<PerformanceEquipe> getPerformanceEquipesByEquipeId(int id) {
        List<PerformanceEquipe> performanceEquipes = new ArrayList<>();
        String sql = "SELECT * FROM performanceequipe WHERE equipe_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    performanceEquipes.add(new PerformanceEquipe(
                            rs.getInt("id"),
                            new TeamService(con).getTeamById(rs.getInt("equipe_id")),
                            new ServiceTournois().get(rs.getInt("tournois_id")),
                            rs.getInt("victoires"),
                            rs.getInt("pertes"),
                            rs.getInt("rang")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des PerformanceEquipe : " + e.getMessage());
        }
        return performanceEquipes;
    }

    public int getTotalVictoires(Equipe equipe) throws SQLException {
        String sql = "SELECT SUM(victoires) FROM performanceequipe WHERE equipe_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, equipe.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des victoires totales : " + e.getMessage());
            throw e;
        }
        return 0;
    }

    public int getTotalPertes(Equipe equipe) throws SQLException {
        String sql = "SELECT SUM(pertes) FROM performanceequipe WHERE equipe_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, equipe.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des pertes totales : " + e.getMessage());
            throw e;
        }
        return 0;
    }

    public int getTotalRang(Equipe equipe) throws SQLException {
        String sql = "SELECT SUM(rang) FROM performanceequipe WHERE equipe_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, equipe.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des rangs totaux : " + e.getMessage());
            throw e;
        }
        return 0;
    }

    public List<PerformanceEquipe> rechercheDynamique(String recherche) throws SQLException {
        String sql = "SELECT pe.*, e.nom AS equipe_nom, t.nom AS tournois_nom " +
                "FROM performanceequipe pe " +
                "JOIN equipe e ON pe.equipe_id = e.id " +
                "JOIN tournois t ON pe.tournois_id = t.id " +
                "WHERE pe.id LIKE ? OR e.nom LIKE ? OR t.nom LIKE ? OR pe.victoires LIKE ? OR pe.pertes LIKE ? OR pe.rang LIKE ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            String searchValue = "%" + recherche + "%";
            for (int i = 1; i <= 6; i++) {
                statement.setString(i, searchValue);
            }

            ResultSet resultSet = statement.executeQuery();
            List<PerformanceEquipe> filteredList = new ArrayList<>();
            TeamService teamService = new TeamService(con);
            ServiceTournois serviceTournois = new ServiceTournois();

            while (resultSet.next()) {
                Team equipe = teamService.getTeamById(resultSet.getInt("equipe_id"));
                Tournois tournois = serviceTournois.get(resultSet.getInt("tournois_id"));

                PerformanceEquipe performance = new PerformanceEquipe(
                        resultSet.getInt("id"),
                        equipe,
                        tournois,
                        resultSet.getInt("victoires"),
                        resultSet.getInt("pertes"),
                        resultSet.getInt("rang")
                );

                filteredList.add(performance);
            }
            return filteredList;
        }
    }
}