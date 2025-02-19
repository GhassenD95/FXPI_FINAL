package services.module4;

import models.module1.Equipe;
import models.module4.PerformanceEquipe;
import models.module4.Tournois;
import services.BaseService;
import services.IService1;
import services.module1.ServiceEquipe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Service1PerformanceEquipe extends BaseService implements IService1<PerformanceEquipe> {

    @Override
    public void add(PerformanceEquipe performanceEquipe) throws SQLException {
        String sql = "INSERT INTO performanceequipe(equipe_id, tournois_id, victoires, pertes, rang) VALUES (?,?,?,?,?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, performanceEquipe.getEquipe().getId());
        stmt.setInt(2, performanceEquipe.getTournois().getId());
        stmt.setInt(3, performanceEquipe.getVictoires());
        stmt.setInt(4, performanceEquipe.getPertes());
        stmt.setInt(5, performanceEquipe.getRang());

        stmt.executeUpdate();
        System.out.println("Performance equipe ajoute");
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
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            PerformanceEquipe performanceEquipe = new PerformanceEquipe();
            performanceEquipe.setId(rs.getInt("id"));
            performanceEquipe.setEquipe(new ServiceEquipe().get(rs.getInt("equipe_id")));
            performanceEquipe.setTournois(new Service1Tournois().get(rs.getInt("tournois_id")));
            performanceEquipe.setVictoires(rs.getInt("victoires"));
            performanceEquipe.setPertes(rs.getInt("pertes"));
            performanceEquipe.setRang(rs.getInt("rang"));

            return performanceEquipe;
        }
        return null;
    }

    @Override
    public List<PerformanceEquipe> getAll() throws SQLException {
        List<PerformanceEquipe> performanceEquipes = new ArrayList<>();
        String sql = "SELECT * FROM performanceequipe";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            PerformanceEquipe performanceEquipe = new PerformanceEquipe();
            performanceEquipe.setId(rs.getInt("id"));
            performanceEquipe.setEquipe(new ServiceEquipe().get(rs.getInt("equipe_id")));
            performanceEquipe.setTournois(new Service1Tournois().get(rs.getInt("tournois_id")));
            performanceEquipe.setVictoires(rs.getInt("victoires"));
            performanceEquipe.setPertes(rs.getInt("pertes"));
            performanceEquipe.setRang(rs.getInt("rang"));

            performanceEquipes.add(performanceEquipe);
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
            System.out.println("PerformanceEquipe has been updated!");
        } catch (SQLException e) {
            System.err.println("SQL error while updating PerformanceEquipe: " + e.getMessage());
            throw e;
        }
    }


    public List<Equipe> getAllEquipes() throws SQLException {
        List<Equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipe";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Equipe equipe = new Equipe();
            equipe.setId(rs.getInt("id"));
            equipe.setNom(rs.getString("nom"));
            equipes.add(equipe);
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
            System.err.println("SQL error while retrieving all Tournois: " + e.getMessage());
        }
        return tournoisList;
    }

    public List<PerformanceEquipe> getPerformanceEquipesByEquipeId(int id) {
        List<PerformanceEquipe> performanceEquipes = new ArrayList<>();
        String sql = "SELECT * FROM performanceequipe WHERE equipe_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PerformanceEquipe performanceEquipe = new PerformanceEquipe();
                    performanceEquipe.setId(rs.getInt("id"));
                    performanceEquipe.setEquipe(new Equipe()); // Avoid recursive call
                    performanceEquipe.getEquipe().setId(rs.getInt("equipe_id"));
                    performanceEquipe.setTournois(new Tournois()); // Avoid recursive call
                    performanceEquipe.getTournois().setId(rs.getInt("tournois_id"));
                    performanceEquipe.setVictoires(rs.getInt("victoires"));
                    performanceEquipe.setPertes(rs.getInt("pertes"));
                    performanceEquipe.setRang(rs.getInt("rang"));
                    performanceEquipes.add(performanceEquipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error while retrieving PerformanceEquipe: " + e.getMessage());
        }
        return performanceEquipes;
    }

}