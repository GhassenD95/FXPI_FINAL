package services.module1;

import enums.Role;
import enums.Status;
import models.module1.Utilisateur;
import services.BaseService;
import services.IService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceUtilisateur extends BaseService implements IService<Utilisateur> {
    @Override
    public void add(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs(prenom, nom, role, birthday, tel, adresse, status, image_url, email, mdp_hash) Values(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, utilisateur.getPrenom());
            ps.setString(2, utilisateur.getNom());
            ps.setString(3, utilisateur.getRole().getRoleName());
            java.sql.Date sqlDate = new java.sql.Date(utilisateur.getBirthday().getTime());
            ps.setDate(4, sqlDate);
            ps.setString(5, utilisateur.getTel());
            ps.setString(6, utilisateur.getAdresse());
            ps.setString(7, utilisateur.getStatus().getStatusName());
            ps.setString(8, utilisateur.getImage_url());
            ps.setString(9, utilisateur.getEmail());
            ps.setString(10,utilisateur.getMdp_hash());
            ps.executeUpdate();
            System.out.println("Utilisateur a ete ajouter !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET prenom = ?, nom = ?, role = ?, birthday = ?, tel = ?, adresse = ?, status = ?, image_url = ?, email = ?, mdp_hash = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, utilisateur.getPrenom());
            ps.setString(2, utilisateur.getNom());
            ps.setString(3, utilisateur.getRole().getRoleName());
            ps.setDate(4, new java.sql.Date(utilisateur.getBirthday().getTime()));
            ps.setString(5, utilisateur.getTel());
            ps.setString(6, utilisateur.getAdresse());
            ps.setString(7, utilisateur.getStatus().getStatusName());
            ps.setString(8, utilisateur.getImage_url());
            ps.setString(9, utilisateur.getEmail());
            ps.setString(10, utilisateur.getMdp_hash());
            ps.setInt(11, utilisateur.getId());

            ps.executeUpdate();
            System.out.println("Utilisateur a ete mis a jour !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, utilisateur.getId());

            ps.executeUpdate();
            System.out.println("Utilisateur a ete supprime !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Utilisateur get(int id) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String prenom = rs.getString("prenom");
                    String nom = rs.getString("nom");
                    Role role = Role.valueOf(rs.getString("role"));
                    Date birthday = rs.getDate("birthday");
                    String tel = rs.getString("tel");
                    String adresse = rs.getString("adresse");
                    Status status = Status.valueOf(rs.getString("status"));
                    String image_url = rs.getString("image_url");
                    String email = rs.getString("email");
                    String mdp_hash = rs.getString("mdp_hash");

                    Utilisateur utilisateur = new Utilisateur(prenom, nom, role, tel, birthday, adresse, status, image_url, email, mdp_hash );
                    utilisateur.setId(id);
                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Utilisateur> getAll() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();

        String sql = "select * from utilisateurs";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String prenom = rs.getString("prenom");
            String nom = rs.getString("nom");
            Role role = Role.valueOf(rs.getString("role"));
            Date birthday = rs.getDate("birthday");
            String tel = rs.getString("tel");
            String adresse = rs.getString("adresse");
            Status status = Status.valueOf(rs.getString("status"));
            String image_url = rs.getString("image_url");
            String email = rs.getString("email");
            String mdp_hash = rs.getString("mdp_hash");

            Utilisateur utilisateur = new Utilisateur(prenom, nom, role, tel, birthday, adresse, status, image_url, email, mdp_hash );
            utilisateurs.add(utilisateur);


        }
        return utilisateurs;
    }
}
