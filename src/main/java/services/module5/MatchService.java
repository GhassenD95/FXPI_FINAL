package services.module5;

import models.module5.MatchSportif;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchService {
    private Connection conn;

    public MatchService(Connection conn) {
        this.conn = conn;
    }

    public void addMatch(MatchSportif match) throws SQLException {
        String sql = "INSERT INTO matchsportif (tournois_id, equipe1_id, equipe2_id, date, lieu) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, match.getTournoisId());
        stmt.setInt(2, match.getEquipe1Id());
        stmt.setInt(3, match.getEquipe2Id());
        stmt.setDate(4, new java.sql.Date(match.getDate().getTime()));
        stmt.setString(5, match.getLieu());
        stmt.executeUpdate();
    }

    public List<MatchSportif> getMatches() throws SQLException {
        List<MatchSportif> matchList = new ArrayList<>();
        String sql = "SELECT * FROM matchsportif";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            matchList.add(new MatchSportif(
                    rs.getInt("id"),
                    rs.getInt("tournois_id"),
                    rs.getInt("equipe1_id"),
                    rs.getInt("equipe2_id"),
                    rs.getDate("date"),
                    rs.getString("lieu")
            ));
        }
        return matchList;
    }

    public void updateMatch(MatchSportif match) throws SQLException {
        String sql = "UPDATE matchsportif SET tournois_id=?, equipe1_id=?, equipe2_id=?, date=?, lieu=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, match.getTournoisId());
        stmt.setInt(2, match.getEquipe1Id());
        stmt.setInt(3, match.getEquipe2Id());
        stmt.setDate(4, new java.sql.Date(match.getDate().getTime()));
        stmt.setString(5, match.getLieu());
        stmt.setInt(6, match.getId());
        stmt.executeUpdate();
    }

    public void deleteMatch(int id) throws SQLException {
        String sql = "DELETE FROM matchsportif WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
} 