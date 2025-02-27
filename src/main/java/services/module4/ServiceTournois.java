package services.module4;

import models.module4.Tournois;
import enums.Sport;
import services.BaseService;
import services.IService1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceTournois extends BaseService implements IService1<Tournois> {
    @Override
    public void add(Tournois tournois) throws SQLException {
        String sql = "INSERT INTO tournois(nom, sport, datedebut, datefin, adresse) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tournois.getNom());
            ps.setString(2, tournois.getSport().toString()); // Assuming 'sport' is an enum
            ps.setDate(3, new java.sql.Date(tournois.getDateDebut().getTime()));
            ps.setDate(4, new java.sql.Date(tournois.getDateFin().getTime()));
            ps.setString(5, tournois.getAdresse());
            ps.executeUpdate();
            System.out.println("Tournoi has been added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(Tournois tournois) {
        String sql = "UPDATE tournois SET nom = ?, sport = ?, datedebut = ?, datefin = ?, adresse = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tournois.getNom());
            ps.setString(2, tournois.getSport().toString()); // Assuming 'sport' is an enum
            ps.setDate(3, new java.sql.Date(tournois.getDateDebut().getTime()));
            ps.setDate(4, new java.sql.Date(tournois.getDateFin().getTime()));
            ps.setString(5, tournois.getAdresse());
            ps.setInt(6, tournois.getId());
            ps.executeUpdate();
            System.out.println("Tournoi has been updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(Tournois tournois) {
        String sql = "DELETE FROM tournois WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tournois.getId());
            ps.executeUpdate();
            System.out.println("Tournoi has been deleted!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Tournois get(int id) {
        String sql = "SELECT * FROM tournois WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nom");
                    Sport sport = Sport.valueOf(rs.getString("sport")); // Assuming 'sport' is an enum
                    Date dateDebut = rs.getDate("datedebut");
                    Date dateFin = rs.getDate("datefin");
                    String adresse = rs.getString("adresse");

                    Tournois tournois = new Tournois(nom, sport, dateDebut, dateFin, adresse);
                    tournois.setId(id);
                    return tournois;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public List<Tournois> getAll() throws SQLException {
        List<Tournois> tournoisList = new ArrayList<>();
        String sql = "SELECT * FROM tournois";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Sport sport = Sport.valueOf(rs.getString("sport")); // Assuming 'sport' is an enum
                Date dateDebut = rs.getDate("datedebut");
                Date dateFin = rs.getDate("datefin");
                String adresse = rs.getString("adresse");

                Tournois tournois = new Tournois(nom, sport, dateDebut, dateFin, adresse);
                tournois.setId(id);
                tournoisList.add(tournois);
            }
        }
        return tournoisList;

    }
    public List<Tournois> tri() throws SQLException {
        String req = "SELECT * FROM Tournois ORDER BY dateDebut";
        Statement ste = con.createStatement();
        ResultSet res = ste.executeQuery(req);

        List<Tournois> list = new ArrayList<>();

        while (res.next()) {
            Tournois t = new Tournois(
                    res.getInt("id"),
                    res.getString("nom"),
                    Sport.valueOf(res.getString("sport")),
                    res.getDate("dateDebut"),
                    res.getDate("dateFin"),
                    res.getString("adresse")
            );

            list.add(t);
        }

        return list;
    }

    public List<Tournois> rechercheDynamique(String recherche) throws SQLException {
        String sql = "SELECT * FROM Tournois WHERE id LIKE ? OR nom LIKE ? OR sport LIKE ? OR dateDebut LIKE ? OR dateFin LIKE ? OR adresse LIKE ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) {
                String searchValue = "%" + recherche + "%";
                statement.setString(i, searchValue);
            }

            ResultSet resultSet = statement.executeQuery();
            List<Tournois> filteredList = new ArrayList<>();
            while (resultSet.next()) {
                Tournois tournois = new Tournois(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        Sport.valueOf(resultSet.getString("sport")),
                        resultSet.getDate("dateDebut"),
                        resultSet.getDate("dateFin"),
                        resultSet.getString("adresse")
                );

                filteredList.add(tournois);
            }
            return filteredList;
        }
    }


}
