module mains.fxpi_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires org.json;
    requires jdk.jsobject;
    requires com.google.gson;
    requires java.sql;
    requires com.google.api.client.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.services.calendar;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;

    opens mains.fxpi_final to javafx.fxml;
    opens models.module4 to javafx.base;
    exports mains.fxpi_final;
    opens controllers to javafx.fxml;
}