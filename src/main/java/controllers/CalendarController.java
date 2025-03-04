package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.module4.Tournois;
import services.module4.ServiceTournois;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable {

    private ZonedDateTime dateFocus;
    private ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    private ServiceTournois serviceTournois = new ServiceTournois();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        month.setText(dateFocus.getMonth().toString() + " " + dateFocus.getYear());
        year.setText(String.valueOf(dateFocus.getYear()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // Get tournaments for the selected month
        Map<Integer, List<Tournois>> tournamentMap = getTournamentMap(dateFocus);

        int monthMaxDate = dateFocus.toLocalDate().lengthOfMonth();
        int dateOffset = dateFocus.withDayOfMonth(1).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        stackPane.getChildren().add(date);

                        // Highlight today's date
                        if (today.getYear() == dateFocus.getYear() &&
                                today.getMonth() == dateFocus.getMonth() &&
                                today.getDayOfMonth() == currentDate) {
                            rectangle.setStroke(Color.BLUE);
                        }

                        // Add tournaments if they exist on this date
                        List<Tournois> tournaments = tournamentMap.get(currentDate);
                        if (tournaments != null) {
                            createTournamentDisplay(tournaments, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createTournamentDisplay(List<Tournois> tournaments, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox tournamentBox = new VBox();
        for (int k = 0; k < tournaments.size(); k++) {
            if (k >= 2) {
                Text moreTournaments = new Text("...");
                tournamentBox.getChildren().add(moreTournaments);
                moreTournaments.setOnMouseClicked(mouseEvent -> {
                    System.out.println("Tournaments on this day: " + tournaments);
                });
                break;
            }
            Text text = new Text(tournaments.get(k).getNom() + " (" + tournaments.get(k).getSport() + ")");
            tournamentBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                System.out.println("Tournament: " + text.getText());
            });
        }
        tournamentBox.setTranslateY((rectangleHeight / 2) * 0.20);
        tournamentBox.setMaxWidth(rectangleWidth * 0.8);
        tournamentBox.setMaxHeight(rectangleHeight * 0.65);
        tournamentBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(tournamentBox);
    }

    private Map<Integer, List<Tournois>> getTournamentMap(ZonedDateTime dateFocus) {
        Map<Integer, List<Tournois>> tournamentMap = new HashMap<>();

        try {
            List<Tournois> tournoisList = serviceTournois.getAll();
            System.out.println("Fetched tournaments: " + tournoisList.size());

            for (Tournois tournois : tournoisList) {
                if (tournois.getDateDebut() == null) {
                    System.out.println("⚠ ERROR: Tournament " + tournois.getNom() + " has a null start date!");
                    continue;
                }

                // Convert java.sql.Date to LocalDate properly
                LocalDate startDate = ((java.sql.Date) tournois.getDateDebut()).toLocalDate();

                System.out.println("Processing: " + tournois.getNom() + " | Date: " + startDate);

                if (startDate.getMonth() == dateFocus.getMonth() && startDate.getYear() == dateFocus.getYear()) {
                    int day = startDate.getDayOfMonth();
                    tournamentMap.computeIfAbsent(day, k -> new ArrayList<>()).add(tournois);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error in getTournamentMap: " + e.getMessage());
            e.printStackTrace();
        }

        return tournamentMap;
    }

}
