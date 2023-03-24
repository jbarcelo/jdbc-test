package com.example;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class Application {
 
    private static Connection connection;
 
    public static void main(String[] args) throws SQLException {
        try {
            openDatabaseConnection();
        } finally {
            closeDatabaseConnection();
        }
    }
 
    private static void openDatabaseConnection() throws SQLException{
        System.out.println("Opening database connection...");
 
        connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/jdbc_demo",
                "example-user", "my_cool_secret"
        );
 
        System.out.println("Connection valid: " + connection.isValid(0));
    }
 
    private static void closeDatabaseConnection() throws SQLException {
        connection.close();
        System.out.println("Connection valid: " + connection.isValid(0));
    }
 
}
