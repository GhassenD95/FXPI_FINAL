package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.StatistiquesAthlete;
import models.StatistiquesEquipe;

public class StatistiquesService {
    private Connection conn;
    private EquipeService equipeService;
    private PerformanceService performanceService;
    private MatchService matchService;

    public StatistiquesService(Connection conn) {
        this.conn = conn;
        this.equipeService = new EquipeService(conn);
        this.performanceService = new PerformanceService(conn);
        this.matchService = new MatchService(conn);
    }

    // Statistiques des équipes
    public List<StatistiquesEquipe> getStatistiquesEquipes() throws SQLException {
        List<StatistiquesEquipe> stats = new ArrayList<>();
        Map<Integer, StatistiquesEquipe> statsMap = new TreeMap<>();

        // Récupérer tous les matchs
        String query = "SELECT m.*, e1.nom as equipe1_nom, e2.nom as equipe2_nom " +
                      "FROM matchsportif m " +
                      "JOIN equipe e1 ON m.equipe1_id = e1.id " +
                      "JOIN equipe e2 ON m.equipe2_id = e2.id";
        
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int equipe1Id = rs.getInt("equipe1_id");
                int equipe2Id = rs.getInt("equipe2_id");
                int butsEquipe1 = rs.getInt("buts_equipe1");
                int butsEquipe2 = rs.getInt("buts_equipe2");
                String equipe1Nom = rs.getString("equipe1_nom");
                String equipe2Nom = rs.getString("equipe2_nom");

                // Mettre à jour les stats de l'équipe 1
                StatistiquesEquipe stats1 = statsMap.computeIfAbsent(equipe1Id, 
                    k -> new StatistiquesEquipe(k, equipe1Nom));
                stats1.ajouterMatch(butsEquipe1, butsEquipe2);

                // Mettre à jour les stats de l'équipe 2
                StatistiquesEquipe stats2 = statsMap.computeIfAbsent(equipe2Id, 
                    k -> new StatistiquesEquipe(k, equipe2Nom));
                stats2.ajouterMatch(butsEquipe2, butsEquipe1);
            }
        }

        stats.addAll(statsMap.values());
        
        // Trier par points (décroissant) puis par différence de buts
        stats.sort(Comparator
            .comparing(StatistiquesEquipe::getPoints).reversed()
            .thenComparing(StatistiquesEquipe::getDifferenceButs).reversed());

        return stats;
    }

    // Statistiques des athlètes
    public List<StatistiquesAthlete> getStatistiquesAthletes() throws SQLException {
        List<StatistiquesAthlete> stats = new ArrayList<>();
        Map<Integer, StatistiquesAthlete> statsMap = new TreeMap<>();

        // Récupérer toutes les performances
        String query = "SELECT p.*, u.nom, u.prenom " +
                      "FROM performanceathlete p " +
                      "JOIN utilisateur u ON p.athlete_id = u.id " +
                      "WHERE u.role = 'ATHLETE'";
        
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int athleteId = rs.getInt("athlete_id");
                String nomComplet = rs.getString("prenom") + " " + rs.getString("nom");

                StatistiquesAthlete statsAthlete = statsMap.computeIfAbsent(athleteId, 
                    k -> new StatistiquesAthlete(k, nomComplet));

                statsAthlete.ajouterPerformance(
                    rs.getInt("minutesJouees"),
                    rs.getInt("buts"),
                    rs.getInt("passesDecisives"),
                    rs.getInt("tirs"),
                    rs.getInt("interceptions"),
                    rs.getInt("fautes"),
                    rs.getInt("cartonsJaunes"),
                    rs.getInt("cartonsRouges"),
                    rs.getInt("rebonds")
                );
            }
        }

        stats.addAll(statsMap.values());
        return stats;
    }

    // Classement des meilleurs buteurs
    public List<StatistiquesAthlete> getMeilleursButeurs() throws SQLException {
        List<StatistiquesAthlete> stats = getStatistiquesAthletes();
        stats.sort(Comparator
            .comparing(StatistiquesAthlete::getButs).reversed()
            .thenComparing(StatistiquesAthlete::getMatchsJoues));
        return stats;
    }

    // Classement des meilleurs passeurs
    public List<StatistiquesAthlete> getMeilleursPasseurs() throws SQLException {
        List<StatistiquesAthlete> stats = getStatistiquesAthletes();
        stats.sort(Comparator
            .comparing(StatistiquesAthlete::getPassesDecisives).reversed()
            .thenComparing(StatistiquesAthlete::getMatchsJoues));
        return stats;
    }

    // Statistiques par match
    public List<StatistiquesAthlete> getStatistiquesParMatch(int matchId) throws SQLException {
        List<StatistiquesAthlete> stats = new ArrayList<>();
        Map<Integer, StatistiquesAthlete> statsMap = new TreeMap<>();

        String query = "SELECT p.*, u.nom, u.prenom " +
                      "FROM performanceathlete p " +
                      "JOIN utilisateur u ON p.athlete_id = u.id " +
                      "WHERE p.match_id = ? AND u.role = 'ATHLETE'";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, matchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int athleteId = rs.getInt("athlete_id");
                    String nomComplet = rs.getString("prenom") + " " + rs.getString("nom");

                    StatistiquesAthlete statsAthlete = new StatistiquesAthlete(athleteId, nomComplet);
                    statsAthlete.ajouterPerformance(
                        rs.getInt("minutesJouees"),
                        rs.getInt("buts"),
                        rs.getInt("passesDecisives"),
                        rs.getInt("tirs"),
                        rs.getInt("interceptions"),
                        rs.getInt("fautes"),
                        rs.getInt("cartonsJaunes"),
                        rs.getInt("cartonsRouges"),
                        rs.getInt("rebonds")
                    );
                    statsMap.put(athleteId, statsAthlete);
                }
            }
        }

        stats.addAll(statsMap.values());
        return stats;
    }
} 