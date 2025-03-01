package services;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime date;
    private String clientName;
    private int clientId;
    private String tournoisName;

    public CalendarActivity(ZonedDateTime date, String clientName, int clientId, String tournoisName) {
        this.date = date;
        this.clientName = clientName;
        this.clientId = clientId;
        this.tournoisName = tournoisName;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public String getClientName() {
        return clientName;
    }

    public int getClientId() {
        return clientId;
    }

    public String getTournoisName() {
        return tournoisName;
    }

    @Override
    public String toString() {
        return tournoisName + " on " + date.toLocalDate();
    }
}