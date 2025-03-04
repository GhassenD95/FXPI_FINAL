package services.module1;

import enums.Role;
import models.module1.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }


    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO utilisateur (prenom, nom, role, birthday, tel, adresse, status, image_url, email, mdp_hash) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, user.getPrenom());
        stmt.setString(2, user.getNom());
        stmt.setString(3, user.getRole().toString());
        stmt.setString(4, user.getBirthday());
        stmt.setString(5, user.getTel());
        stmt.setString(6, user.getAdresse());
        stmt.setString(7, user.getStatus());
        stmt.setString(8, user.getImageUrl());
        stmt.setString(9, user.getEmail());
        stmt.setString(10, user.getMdpHash());
        stmt.executeUpdate();
    }

    public List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            users.add(new User(
                    rs.getInt("id"),
                    rs.getString("prenom"),
                    rs.getString("nom"),
                    User.valueOf(rs.getString("role")),
                    rs.getString("birthday"),
                    rs.getString("tel"),
                    rs.getString("adresse"),
                    rs.getString("status"),
                    rs.getString("image_url"),
                    rs.getString("email"),
                    rs.getString("mdp_hash")
            ));
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String status = (user.getStatus() == null || user.getStatus().isEmpty()) ? "INACTIVE" : user.getStatus();
        String query = "UPDATE utilisateur SET prenom=?, nom=?, role=?, birthday=?, tel=?, adresse=?, status=?, image_url=?, email=?, mdp_hash=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, user.getPrenom());
        stmt.setString(2, user.getNom());
        stmt.setString(3, user.getRole().toString());
        stmt.setString(4, user.getBirthday());
        stmt.setString(5, user.getTel());
        stmt.setString(6, user.getAdresse());
        stmt.setString(7, status);
        stmt.setString(8, user.getImageUrl());
        stmt.setString(9, user.getEmail());
        stmt.setString(10, user.getMdpHash());
        stmt.setInt(11, user.getId());
        stmt.executeUpdate();
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM utilisateur WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
    public User signIn(String email, String password) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedHash = rs.getString("mdp_hash");
            if (BCrypt.checkpw(password, storedHash)) {
                // Build User from DB result
                return new User(
                        rs.getInt("id"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        Role.valueOf(rs.getString("role")),
                        rs.getString("birthday"),
                        rs.getString("tel"),
                        rs.getString("adresse"),
                        rs.getString("status"),
                        rs.getString("image_url"),
                        rs.getString("email"),
                        storedHash
                );
            }
        }
        return null; // Invalid credentials
    }

    public boolean checkEmailExists(String email) throws SQLException {
        String sql = "SELECT id FROM utilisateur WHERE email=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        return rs.next(); // If there's a row, the email is taken
    }
}
