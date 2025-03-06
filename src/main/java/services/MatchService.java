package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.MatchSportif;

public class MatchService {
    private Connection conn;

    public MatchService(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return conn;
    }

    public void addMatch(MatchSportif match) throws SQLException {
        String sql = "INSERT INTO matchsportif (tournois_id, equipe1_id, equipe2_id, date, lieu) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, match.getTournoisId());
        stmt.setInt(2, match.getEquipe1Id());
        stmt.setInt(3, match.getEquipe2Id());
        stmt.setTimestamp(4, match.getTimestamp());
        stmt.setString(5, match.getLieu());
        stmt.executeUpdate();
    }

    public List<MatchSportif> getMatches() throws SQLException {
        List<MatchSportif> matchList = new ArrayList<>();
        String sql = "SELECT * FROM matchsportif";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            MatchSportif match = new MatchSportif(
                    rs.getInt("id"),
                    rs.getInt("tournois_id"),
                    rs.getInt("equipe1_id"),
                    rs.getInt("equipe2_id"),
                    rs.getTimestamp("date"),
                    rs.getString("lieu")
            );
            matchList.add(match);
        }
        return matchList;
    }

    public void updateMatch(MatchSportif match) throws SQLException {
        String sql = "UPDATE matchsportif SET tournois_id=?, equipe1_id=?, equipe2_id=?, date=?, lieu=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, match.getTournoisId());
        stmt.setInt(2, match.getEquipe1Id());
        stmt.setInt(3, match.getEquipe2Id());
        stmt.setTimestamp(4, match.getTimestamp());
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

    public List<String> getAllMatchNames() throws SQLException {
        List<String> matchNames = new ArrayList<>();
        String sql = "SELECT m.id, m.date, m.lieu, e1.nom as equipe1_nom, e2.nom as equipe2_nom " +
                "FROM matchsportif m " +
                "JOIN equipe e1 ON m.equipe1_id = e1.id " +
                "JOIN equipe e2 ON m.equipe2_id = e2.id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String date = rs.getTimestamp("date").toString();
            String lieu = rs.getString("lieu");
            String equipe1 = rs.getString("equipe1_nom");
            String equipe2 = rs.getString("equipe2_nom");
            matchNames.add(equipe1 + " vs " + equipe2 + " - " + date + " - " + lieu);
        }
        return matchNames;
    }

    public String getMatchNameById(int id) throws SQLException {
        String sql = "SELECT m.date, m.lieu, e1.nom as equipe1_nom, e2.nom as equipe2_nom " +
                "FROM matchsportif m " +
                "JOIN equipe e1 ON m.equipe1_id = e1.id " +
                "JOIN equipe e2 ON m.equipe2_id = e2.id " +
                "WHERE m.id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String date = rs.getTimestamp("date").toString();
            String lieu = rs.getString("lieu");
            String equipe1 = rs.getString("equipe1_nom");
            String equipe2 = rs.getString("equipe2_nom");
            return equipe1 + " vs " + equipe2 + " - " + date + " - " + lieu;
        }
        return null;
    }

    public int getMatchIdByName(String matchName) throws SQLException {
        System.out.println("Recherche du match avec le nom: '" + matchName + "'");

        // Extraire les noms des équipes du nom du match
        String[] parts = matchName.split(" vs ");
        if (parts.length != 2) {
            System.out.println("Format de nom de match invalide");
            return -1;
        }

        String equipe1Nom = parts[0].trim();
        String equipe2Nom = parts[1].split(" - ")[0].trim();

        System.out.println("Équipe 1: '" + equipe1Nom + "'");
        System.out.println("Équipe 2: '" + equipe2Nom + "'");

        // Récupérer les IDs des équipes
        String query = "SELECT m.id FROM matchsportif m " +
                "JOIN equipe e1 ON m.equipe1_id = e1.id " +
                "JOIN equipe e2 ON m.equipe2_id = e2.id " +
                "WHERE e1.nom = ? AND e2.nom = ?";

        System.out.println("Requête SQL: " + query);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipe1Nom);
            stmt.setString(2, equipe2Nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Match trouvé avec l'ID: " + id);
                return id;
            }
            System.out.println("Aucun match trouvé avec ces équipes");
        }
        return -1;
    }

    public List<MatchSportif> getAllMatchs() throws SQLException {
        List<MatchSportif> matchs = new ArrayList<>();
        String query = "SELECT * FROM matchsportif";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MatchSportif match = new MatchSportif();
                match.setId(rs.getInt("id"));
                match.setTournoisId(rs.getInt("tournois_id"));
                match.setEquipe1Id(rs.getInt("equipe1_id"));
                match.setEquipe2Id(rs.getInt("equipe2_id"));
                match.setTimestamp(rs.getTimestamp("date"));
                match.setLieu(rs.getString("lieu"));
                matchs.add(match);
            }
        }
        return matchs;
    }

    public MatchSportif getMatchById(int matchId) throws SQLException {
        String query = "SELECT m.*, e1.nom as equipe1_nom, e2.nom as equipe2_nom " +
                "FROM matchsportif m " +
                "JOIN equipe e1 ON m.equipe1_id = e1.id " +
                "JOIN equipe e2 ON m.equipe2_id = e2.id " +
                "WHERE m.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, matchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MatchSportif match = new MatchSportif();
                match.setId(rs.getInt("id"));
                match.setTournoisId(rs.getInt("tournois_id"));
                match.setEquipe1Id(rs.getInt("equipe1_id"));
                match.setEquipe2Id(rs.getInt("equipe2_id"));
                match.setTimestamp(rs.getTimestamp("date"));
                match.setLieu(rs.getString("lieu"));
                match.setEquipe1(rs.getString("equipe1_nom"));
                match.setEquipe2(rs.getString("equipe2_nom"));
                return match;
            }
        }
        return null;
    }



}
