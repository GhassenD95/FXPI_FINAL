package services.module1;

import models.module1.Team;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamService {
    private Connection conn;

    public TeamService(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public void addTeam(Team team) throws SQLException {
        String sql = "INSERT INTO equipe (nom, division, coach_id, sport) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, team.getNom());
        stmt.setString(2, team.getDivision());
        stmt.setInt(3, team.getCoachId());
        stmt.setString(4, team.getSport());
        stmt.executeUpdate();
    }

    // READ
    public List<Team> getTeams() throws SQLException {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT * FROM equipe";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            teamList.add(new Team(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("division"),
                    rs.getInt("coach_id"),
                    rs.getString("sport")
            ));
        }
        return teamList;
    }

    // UPDATE
    public void updateTeam(Team team) throws SQLException {
        String sql = "UPDATE equipe SET nom=?, division=?, coach_id=?, sport=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, team.getNom());
        stmt.setString(2, team.getDivision());
        stmt.setInt(3, team.getCoachId());
        stmt.setString(4, team.getSport());
        stmt.setInt(5, team.getId());
        stmt.executeUpdate();
    }

    // DELETE
    public void deleteTeam(int id) throws SQLException {
        String sql = "DELETE FROM equipe WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
    public Team getTeamById(int id) throws SQLException {
        String sql = "SELECT * FROM equipe WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Team(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("division"),
                    rs.getInt("coach_id"),
                    rs.getString("sport")
            );
        }
        return null; // Return null if no team is found
    }


}
