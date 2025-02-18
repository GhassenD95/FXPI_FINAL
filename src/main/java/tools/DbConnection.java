package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private final String URL = "jdbc:mysql://localhost:3306/pi_db";
    private final String USER = "root";
    private final String Password = "";

    private Connection conn = null;

    private static DbConnection instance;

    private DbConnection() {
        try {
            conn = DriverManager.getConnection(URL, USER, Password);
            System.out.println("connection etablie");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }


    public Connection getConn() {
        return conn;
    }

}
