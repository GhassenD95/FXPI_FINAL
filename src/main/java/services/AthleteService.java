package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AthleteService {
    private Connection conn;

    public AthleteService(Connection conn) {
        this.conn = conn;
    }

    public String getEquipeAthlete(int athleteId) throws SQLException {
        String query = "SELECT e.nom FROM utilisateur u " +
                      "JOIN equipe e ON u.equipe_id = e.id " +
                      "WHERE u.id = ? AND u.role = 'ATHLETE'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, athleteId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            }
        }
        return null;
    }

    public int getAthleteIdByName(String athleteName) throws SQLException {
        String[] parts = athleteName.split(" ", 2);
        if (parts.length != 2) {
            throw new SQLException("Format de nom invalide");
        }
        String prenom = parts[0];
        String nom = parts[1];
        
        String query = "SELECT id FROM utilisateur WHERE prenom = ? AND nom = ? AND role = 'ATHLETE'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, prenom);
            stmt.setString(2, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                throw new SQLException("Athlète non trouvé");
            }
        }
    }
} 