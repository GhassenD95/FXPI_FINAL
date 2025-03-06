package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TournoiService {
    private Connection conn;

    public TournoiService(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAllTournoiNames() throws SQLException {
        List<String> tournoiNames = new ArrayList<>();
        String sql = "SELECT nom FROM tournois ORDER BY nom";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            tournoiNames.add(rs.getString("nom"));
        }
        return tournoiNames;
    }

    public int getTournoiIdByName(String nom) throws SQLException {
        String sql = "SELECT id FROM tournois WHERE nom = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nom);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }
        return -1;
    }

    public String getTournoiNameById(int id) throws SQLException {
        String sql = "SELECT nom FROM tournois WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("nom");
        }
        return null;
    }
} 