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
        // Hash only if the password is not already hashed
        String hashedPassword = BCrypt.hashpw(user.getRawPassword(), BCrypt.gensalt());
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
        stmt.setString(10, hashedPassword); // Save only the BCrypt hash
        stmt.executeUpdate();
        System.out.println("Raw password being hashed: " + user.getRawPassword());
        System.out.println("Generated bcrypt hash: " + hashedPassword);

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
        // Fetch the existing password if it's not provided
        String currentPassword = user.getMdpHash();

        if (currentPassword == null || currentPassword.isEmpty()) {
            String queryCheck = "SELECT mdp_hash FROM utilisateur WHERE id=?";
            try (PreparedStatement stmtCheck = conn.prepareStatement(queryCheck)) {
                stmtCheck.setInt(1, user.getId());
                ResultSet rs = stmtCheck.executeQuery();
                if (rs.next()) {
                    currentPassword = rs.getString("mdp_hash");
                }
            }
        } else if (!(currentPassword.startsWith("$2a$") || currentPassword.startsWith("$2b$") || currentPassword.startsWith("$2y$"))) {
            // Hash only if the password is not already hashed
            currentPassword = BCrypt.hashpw(currentPassword, BCrypt.gensalt());
        }

        String query = "UPDATE utilisateur SET prenom=?, nom=?, role=?, birthday=?, tel=?, adresse=?, status=?, image_url=?, email=?, mdp_hash=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getPrenom());
            stmt.setString(2, user.getNom());
            stmt.setString(3, user.getRole().toString());
            stmt.setString(4, user.getBirthday());
            stmt.setString(5, user.getTel());
            stmt.setString(6, user.getAdresse());
            stmt.setString(7, user.getStatus() == null || user.getStatus().isEmpty() ? "INACTIVE" : user.getStatus());
            stmt.setString(8, user.getImageUrl());
            stmt.setString(9, user.getEmail());
            stmt.setString(10, currentPassword);
            stmt.setInt(11, user.getId());
            stmt.executeUpdate();
        }
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
            System.out.println("Stored hash: " + storedHash); // Log the stored hash for debugging
            System.out.println("Password to check: " + password); // Log the password for debugging
            System.out.println("Trimmed password to check: " + password.trim()); // Log the trimmed password for debugging
            try {
                if (BCrypt.checkpw(password.trim(), storedHash)) {
                    System.out.println("Password matches!"); // Log successful match
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
                } else {
                    System.out.println("Password does not match!"); // Log unsuccessful match
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid salt version: " + e.getMessage());
            }
        } else {
            System.out.println("No user found with email: " + email); // Log if no user is found
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
