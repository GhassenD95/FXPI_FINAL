package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.PerformanceAthlete;

public class PerformanceService {
    private Connection conn;

    public PerformanceService(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public void addPerformance(PerformanceAthlete performance) throws SQLException {
        // Vérifier si une performance existe déjà
        if (performanceExiste(performance.getAthleteId(), performance.getMatchId())) {
            throw new SQLException("Une performance existe déjà pour cet athlète dans ce match");
        }

        String sql = "INSERT INTO performanceathlete (athlete_id, match_id, minutesJouees, buts, passesDecisives, tirs, interceptions, fautes, cartonsJaunes, cartonsRouges, rebonds) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, performance.getAthleteId());
        stmt.setInt(2, performance.getMatchId());
        stmt.setInt(3, performance.getMinutesJouees());
        stmt.setInt(4, performance.getButs());
        stmt.setInt(5, performance.getPassesDecisives());
        stmt.setInt(6, performance.getTirs());
        stmt.setInt(7, performance.getInterceptions());
        stmt.setInt(8, performance.getFautes());
        stmt.setInt(9, performance.getCartonsJaunes());
        stmt.setInt(10, performance.getCartonsRouges());
        stmt.setInt(11, performance.getRebonds());
        stmt.executeUpdate();
    }

    // READ
    public List<PerformanceAthlete> getPerformances() throws SQLException {
        List<PerformanceAthlete> performanceList = new ArrayList<>();
        String sql = "SELECT * FROM performanceathlete";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            performanceList.add(new PerformanceAthlete(
                    rs.getInt("id"),
                    rs.getInt("athlete_id"),
                    rs.getInt("match_id"),
                    rs.getInt("minutesJouees"),
                    rs.getInt("buts"),
                    rs.getInt("passesDecisives"),
                    rs.getInt("tirs"),
                    rs.getInt("interceptions"),
                    rs.getInt("fautes"),
                    rs.getInt("cartonsJaunes"),
                    rs.getInt("cartonsRouges"),
                    rs.getInt("rebonds")
            ));
        }
        return performanceList;
    }

    // UPDATE
    public void updatePerformance(PerformanceAthlete performance) throws SQLException {
        String sql = "UPDATE performanceathlete SET athlete_id=?, match_id=?, minutesJouees=?, buts=?, passesDecisives=?, tirs=?, interceptions=?, fautes=?, cartonsJaunes=?, cartonsRouges=?, rebonds=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, performance.getAthleteId());
        stmt.setInt(2, performance.getMatchId());
        stmt.setInt(3, performance.getMinutesJouees());
        stmt.setInt(4, performance.getButs());
        stmt.setInt(5, performance.getPassesDecisives());
        stmt.setInt(6, performance.getTirs());
        stmt.setInt(7, performance.getInterceptions());
        stmt.setInt(8, performance.getFautes());
        stmt.setInt(9, performance.getCartonsJaunes());
        stmt.setInt(10, performance.getCartonsRouges());
        stmt.setInt(11, performance.getRebonds());
        stmt.setInt(12, performance.getId());
        stmt.executeUpdate();
    }

    // DELETE
    public void deletePerformance(int id) throws SQLException {
        String sql = "DELETE FROM performanceathlete WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public List<String> getAllAthleteNames() throws SQLException {
        List<String> athleteNames = new ArrayList<>();
        String query = "SELECT nom, prenom FROM utilisateur WHERE role = 'ATHLETE'";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                athleteNames.add(prenom + " " + nom);
            }
        }
        return athleteNames;
    }

    public int getAthleteIdByName(String fullName) throws SQLException {
        System.out.println("Recherche de l'athlète avec le nom: '" + fullName + "'");
        String[] parts = fullName.split(" ", 2);
        if (parts.length != 2) {
            System.out.println("Format de nom invalide: " + fullName);
            throw new SQLException("Format de nom invalide");
        }
        String prenom = parts[0];
        String nom = parts[1];
        System.out.println("Prénom: '" + prenom + "', Nom: '" + nom + "'");
        
        String query = "SELECT id FROM utilisateur WHERE prenom = ? AND nom = ? AND role = 'ATHLETE'";
        System.out.println("Requête SQL: " + query);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, prenom);
            stmt.setString(2, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    System.out.println("Athlète trouvé avec l'ID: " + id);
                    return id;
                }
                System.out.println("Aucun athlète trouvé avec ce nom");
                throw new SQLException("Athlète non trouvé");
            }
        }
    }

    public String getAthleteNameById(int athleteId) throws SQLException {
        String query = "SELECT nom, prenom FROM utilisateur WHERE id = ? AND role = 'ATHLETE'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String prenom = rs.getString("prenom");
                    String nom = rs.getString("nom");
                    return prenom + " " + nom;
                }
                throw new SQLException("Athlète non trouvé");
            }
        }
    }

    public boolean performanceExiste(int athleteId, int matchId) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM performanceathlete WHERE athlete_id = ? AND match_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);
            stmt.setInt(2, matchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        return false;
    }
}
