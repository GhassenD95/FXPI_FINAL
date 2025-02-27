package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import models.module4.PerformanceEquipe;
import services.module4.ServicePerformanceEquipe;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformanceChartsController {

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    public void initialize() {
        updateCharts();
    }

    private void updateCharts() {
        ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
        try {
            List<PerformanceEquipe> performances = servicePerformanceEquipe.getAll();

            if (performances == null || performances.isEmpty()) {
                return; // Handle the case where no data is available
            }

            // Create a map to store cumulative stats per team
            Map<String, TeamStats> teamStatsMap = new HashMap<>();

            // Process each performance entry
            for (PerformanceEquipe performance : performances) {
                String teamName = performance.getEquipe().getNom();
                int wins = performance.getVictoires();
                int losses = performance.getPertes();
                int rank = performance.getRang();

                // Aggregate wins, losses, and ranks for the same team
                TeamStats stats = teamStatsMap.getOrDefault(teamName, new TeamStats());
                stats.addStats(wins, losses, rank);
                teamStatsMap.put(teamName, stats);
            }

            // Prepare PieChart Data (Top Teams - Most Wins, Least Losses)
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            double totalWins = 0;

            for (Map.Entry<String, TeamStats> entry : teamStatsMap.entrySet()) {
                String teamName = entry.getKey();
                TeamStats stats = entry.getValue();
                double averageWins = stats.getAverageWins();
                double averageLosses = stats.getAverageLosses();
                double totalRank = stats.getSumRank();

                // Add pie chart data for teams with more wins than losses
                if (averageWins > averageLosses) {
                    PieChart.Data data = new PieChart.Data(teamName, averageWins);
                    double percentage = (averageWins / totalWins) * 100;
                    data.setName(teamName + " - Wins: " + averageWins + " (" + String.format("%.2f%%", percentage) + ")");
                    pieChartData.add(data);
                }

                totalWins += averageWins; // For percentage calculation
            }

            pieChart.setData(pieChartData);

            // Prepare BarChart Data (Top 3 Team Rankings: Least Rank is Top)
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Top 3 Team Rankings");

            // Sort teams by rank (lowest rank is the top)
            performances.sort((p1, p2) -> Integer.compare(p1.getRang(), p2.getRang()));

            // Display only the top 3 teams
            for (int i = 0; i < Math.min(3, performances.size()); i++) {
                PerformanceEquipe performance = performances.get(i);
                String teamName = performance.getEquipe().getNom();
                int rank = performance.getRang();
                series.getData().add(new XYChart.Data<>(teamName, rank));
            }

            barChart.getData().clear();
            barChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper class to store and calculate the aggregate stats for each team
    private static class TeamStats {
        private int totalWins;
        private int totalLosses;
        private int totalRank;
        private int count;

        public void addStats(int wins, int losses, int rank) {
            this.totalWins += wins;
            this.totalLosses += losses;
            this.totalRank += rank;
            this.count++;
        }

        public double getAverageWins() {
            return totalWins / (double) count;
        }

        public double getAverageLosses() {
            return totalLosses / (double) count;
        }

        public double getSumRank() {
            return totalRank;
        }
    }
}
