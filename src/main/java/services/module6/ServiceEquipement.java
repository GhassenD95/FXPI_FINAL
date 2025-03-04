package services.module6;

import models.module6.Equipement;
import enums.EtatEquipement;
import enums.TypeEquipement;
import tools.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEquipement {
    private Connection conn;

    public ServiceEquipement() {
        conn = DbConnection.getInstance().getConn();
    }
    public void ajouterEquipement(Equipement equipement) throws SQLException {
        String query = "INSERT INTO equipement (nom, description, etat, typeEquipement, image_url, quantite) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, equipement.getNom());
        pst.setString(2, equipement.getDescription());
        pst.setString(3, equipement.getEtat().name());
        pst.setString(4, equipement.getTypeEquipement().name());
        pst.setString(5, equipement.getImage_url());
        pst.setInt(6, equipement.getQuantite());
        pst.executeUpdate();
    }

    public void updateEquipement(Equipement equipement) throws SQLException {
        String query = "UPDATE equipement SET nom = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, equipement.getNom());
            stmt.setString(2, equipement.getDescription());
            stmt.setInt(3, equipement.getId());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated + " ligne(s) mise(s) à jour.");
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            throw e;
        }
    }


    public void supprimerEquipement(int id) throws SQLException {
        String query = "DELETE FROM equipement WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public List<Equipement> getAllEquipements() throws SQLException {
        List<Equipement> equipements = new ArrayList<>();
        String query = "SELECT * FROM equipement";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Equipement equipement = new Equipement(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    EtatEquipement.valueOf(rs.getString("etat")),
                    TypeEquipement.valueOf(rs.getString("typeEquipement")),
                    rs.getString("image_url"),
                    rs.getInt("quantite")
            );
            equipements.add(equipement);
        }
        return equipements;
    }

    public void modifierEquipement(Equipement selectedEquipement) {
        
    }
}
