package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeService {
    private Connection conn;

    public EquipeService(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAllEquipeNames() throws SQLException {
        List<String> equipeNames = new ArrayList<>();
        String sql = "SELECT nom FROM equipe ORDER BY nom";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            equipeNames.add(rs.getString("nom"));
        }
        return equipeNames;
    }

    public int getEquipeIdByName(String nom) throws SQLException {
        String sql = "SELECT id FROM equipe WHERE nom = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nom);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }
        return -1;
    }

    public String getEquipeNameById(int id) throws SQLException {
        String sql = "SELECT nom FROM equipe WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("nom");
        }
        return null;
    }
} 