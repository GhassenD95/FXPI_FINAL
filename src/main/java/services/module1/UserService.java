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
        String hashedPassword = BCrypt.hashpw(user.getMdpHash(), BCrypt.gensalt());
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
        stmt.setString(10, hashedPassword);
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
        // Check if the password is already hashed. BCrypt hashes typically start with "$2a$", "$2b$", or "$2y$".
        String passwordToStore = user.getMdpHash();
        if (!(passwordToStore.startsWith("$2a$") || passwordToStore.startsWith("$2b$") || passwordToStore.startsWith("$2y$"))) {
            // If not, hash it before storing
            passwordToStore = BCrypt.hashpw(passwordToStore, BCrypt.gensalt());
        }

        // If status is null, default to "INACTIVE"
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
        stmt.setString(10, passwordToStore);
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
            // Trim the password input
            String inputPassword = password.trim();

            System.out.println("Plain text password: '" + inputPassword + "'");
            System.out.println("Stored hash: " + storedHash);

            if (BCrypt.checkpw(inputPassword, storedHash)) {
                System.out.println("Password matches!");
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
                System.out.println("Password does not match!");
            }
        } else {
            System.out.println("No user found with email: " + email);
        }
        return null;
    }



    public boolean checkEmailExists(String email) throws SQLException {
        String sql = "SELECT id FROM utilisateur WHERE email=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        return rs.next(); // If there's a row, the email is taken
    }
}
