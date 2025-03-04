package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    private Button btnGenerateReport;

    @FXML
    private TextArea txtReport;

    @FXML
    public void initialize() {
        updateCharts();
        addPieChartClickListener();  // Add listener to the pie chart
    }

    private void updateCharts() {
        ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
        try {
            List<PerformanceEquipe> performances = servicePerformanceEquipe.getAll();

            if (performances == null || performances.isEmpty()) {
                return;
            }

            // Map to store the sum of wins, losses, and rank count for each team
            Map<String, TeamStats> teamStatsMap = new HashMap<>();
            double totalWins = 0;
            double totalLosses = 0;

            // Process performance data for each team
            for (PerformanceEquipe performance : performances) {
                String teamName = performance.getEquipe().getNom();
                int wins = performance.getVictoires();
                int losses = performance.getPertes();
                int rank = performance.getRang();

                TeamStats stats = teamStatsMap.getOrDefault(teamName, new TeamStats());
                stats.addStats(wins, losses, rank);
                teamStatsMap.put(teamName, stats);

                totalWins += wins;
                totalLosses += losses;
            }

            // Pie chart for win/loss ratio
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Map.Entry<String, TeamStats> entry : teamStatsMap.entrySet()) {
                String teamName = entry.getKey();
                TeamStats stats = entry.getValue();
                double averageWins = stats.getAverageWins();
                double averageLosses = stats.getAverageLosses();

                PieChart.Data winData = new PieChart.Data(teamName + " Wins", averageWins);
                PieChart.Data lossData = new PieChart.Data(teamName + " Losses", averageLosses);

                pieChartData.add(winData);
                pieChartData.add(lossData);
            }

            pieChart.setData(pieChartData);

            // Sorting teams by their average rank (ascending order, lower rank is better)
            ObservableList<TeamRank> teamRanks = FXCollections.observableArrayList();
            for (Map.Entry<String, TeamStats> entry : teamStatsMap.entrySet()) {
                String teamName = entry.getKey();
                TeamStats stats = entry.getValue();
                double averageRank = stats.getAverageRank();
                teamRanks.add(new TeamRank(teamName, averageRank));
            }

            // Sort by the average rank (ascending order)
            teamRanks.sort((t1, t2) -> Double.compare(t1.getAverageRank(), t2.getAverageRank()));

            // Bar chart for top 3 teams based on the average rank
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Top 3 Team Rankings");

            // Add the top 3 teams based on average rank
            for (int i = 0; i < Math.min(3, teamRanks.size()); i++) {
                TeamRank teamRank = teamRanks.get(i);
                series.getData().add(new XYChart.Data<>(teamRank.getTeamName(), teamRank.getAverageRank()));
            }

            barChart.getData().clear();
            barChart.getData().add(series);

            // Generating report for top 3 teams based on the average rank
            StringBuilder report = new StringBuilder();
            report.append("Top 3 Teams Based on Average Ranking:\n");

            for (int i = 0; i < Math.min(3, teamRanks.size()); i++) {
                TeamRank teamRank = teamRanks.get(i);
                report.append(i + 1).append(". ").append(teamRank.getTeamName())
                        .append(" - Average Rank: ").append(String.format("%.2f", teamRank.getAverageRank())).append("\n");
            }

            txtReport.setText(report.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper class to store team name and average rank
    private static class TeamRank {
        private String teamName;
        private double averageRank;

        public TeamRank(String teamName, double averageRank) {
            this.teamName = teamName;
            this.averageRank = averageRank;
        }

        public String getTeamName() {
            return teamName;
        }

        public double getAverageRank() {
            return averageRank;
        }
    }

    // Helper class to accumulate statistics for each team
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

        public double getAverageRank() {
            return totalRank / (double) count;
        }
    }

    // Add listener to the pie chart to display percentage and team name on click
    private void addPieChartClickListener() {
        // Add an event handler to each data slice in the pie chart
        for (PieChart.Data slice : pieChart.getData()) {
            slice.getNode().setOnMouseClicked(event -> {
                // Get the team name and percentage when the slice is clicked
                String teamName = slice.getName().split(" ")[0];  // Extract team name (before "Wins" or "Losses")
                double percentage = (slice.getPieValue() / getTotalWinsLosses()) * 100;

                // Display the team name and percentage in the report or text area
                txtReport.setText("Team: " + teamName + "\nPourrcentage de victoires: " + String.format("%.2f", percentage) + "%");
            });
        }
    }


    // Helper method to calculate the total number of wins and losses for all teams
    private double getTotalWinsLosses() {
        double total = 0;
        for (PieChart.Data data : pieChart.getData()) {
            total += data.getPieValue();
        }
        return total;
    }
}
