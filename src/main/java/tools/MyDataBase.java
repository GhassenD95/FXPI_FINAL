package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private static MyDataBase instance;
    private Connection cnx;
    
    private final String URL = "jdbc:mysql://localhost:3306/dbpi";
    private final String USER = "root";
    private final String PASSWORD = "";
    
    private MyDataBase() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion établie!");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public static MyDataBase getInstance() {
        if(instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }
    
    public Connection getCnx() {
        return cnx;
    }
}
