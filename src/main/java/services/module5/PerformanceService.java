package services.module5;

import models.module5.PerformanceAthlete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceService {
    private Connection conn;

    public PerformanceService(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public void addPerformance(PerformanceAthlete performance) throws SQLException {
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
}
