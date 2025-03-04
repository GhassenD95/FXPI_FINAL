package services.module6;

import models.module6.InstallationSportive;
import enums.TypeInstallation;
import tools.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceInstallationSportive {

    private Connection conn;

    public ServiceInstallationSportive() {
        conn = DbConnection.getInstance().getConn();
    }

        public List<InstallationSportive> getAllInstallations() throws SQLException {
        List<InstallationSportive> installations = new ArrayList<>();
        String query = "SELECT * FROM installationsportive";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            InstallationSportive installation = new InstallationSportive(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    TypeInstallation.valueOf(rs.getString("typeInstallation")),  // ✅ Correct column name
                    rs.getString("adresse"),
                    rs.getInt("capacite"),
                    rs.getBoolean("isDisponible"),  // ✅ Changed to isDisponible
                    rs.getString("image_url")
            );
            installations.add(installation);
        }
        return installations;
    }

    public void ajouterInstallation(InstallationSportive installation) throws SQLException {
        String query = "INSERT INTO installationsportive (nom, typeInstallation, adresse, capacite, isDisponible, image_url) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, installation.getNom());
        pst.setString(2, installation.getTypeInstallation().name()); // ✅ Store enum as String
        pst.setString(3, installation.getAdresse());
        pst.setInt(4, installation.getCapacite());
        pst.setBoolean(5, installation.isDisponible());  // ✅ Corrected column name
        pst.setString(6, installation.getImage_url());

        pst.executeUpdate();
    }

    public void modifierInstallation(InstallationSportive installation) throws SQLException {
        String query = "UPDATE installationsportive SET nom = ?, typeInstallation = ?, adresse = ?, capacite = ?, isDisponible = ?, image_url = ? WHERE id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, installation.getNom());
        pst.setString(2, installation.getTypeInstallation().name()); // ✅ Store enum as String
        pst.setString(3, installation.getAdresse());
        pst.setInt(4, installation.getCapacite());
        pst.setBoolean(5, installation.isDisponible());  // ✅ Corrected column name
        pst.setString(6, installation.getImage_url());
        pst.setInt(7, installation.getId());
        pst.executeUpdate();
    }

    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM installationsportive WHERE id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public List<InstallationSportive> getAll() {
        List<InstallationSportive> installations = new ArrayList<>();
        try {
            String query = "SELECT * FROM installationsportive";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                InstallationSportive installation = new InstallationSportive(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        TypeInstallation.valueOf(rs.getString("typeInstallation")), // ✅ Ensure correct column name
                        rs.getString("adresse"),
                        rs.getInt("capacite"),
                        rs.getBoolean("isDisponible"), // ✅ Changed column name to match database
                        rs.getString("image_url")
                );
                installations.add(installation);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error instead of failing silently
        }
        return installations;
    }
}
