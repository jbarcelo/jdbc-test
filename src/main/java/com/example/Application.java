package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
 
import java.sql.*;
 
public class Application {
 
    private static HikariDataSource dataSource;
 
    public static void main(String[] args) throws SQLException {
        try {
            initDatabaseConnectionPool();
            deleteData("%");
            readData();
            createData("Java", 10);
            createData("JavaScript", 9);
            createData("C++", 8);
            readData();
            updateData("C++", 7);
            readData();
            deleteData("C++");
            readData();
        } finally {
            closeDatabaseConnectionPool();
        }
    }
 
    private static void initDatabaseConnectionPool() {
       dataSource = new HikariDataSource();
       dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/jdbc_demo");
       dataSource.setUsername("example-user");
       dataSource.setPassword("my_cool_secret");
    }
 
    private static void closeDatabaseConnectionPool() {
        dataSource.close();
    }
 
    private static void createData(String name, int rating) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO programming_language(name, rating)
                        VALUES (?, ?)
                        """)) {
                statement.setString(1, name);
                statement.setInt(2, rating);
                int rowsInserted = statement.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted);
            }
        }
    }

    private static void readData() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        SELECT name, rating
                        FROM programming_language
                        ORDER BY rating DESC
                    """)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String name = resultSet.getString("name");
                        int rating = resultSet.getInt("rating");
                        System.out.println("\t> " + name + ": " + rating);
                    }
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            }
        }
    }

    private static void updateData(String name, int newRating) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        UPDATE programming_language
                        SET rating = ?
                        WHERE name = ?
                    """)) {
                statement.setInt(1, newRating);
                statement.setString(2, name);
                int rowsUpdated = statement.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);
            }
        }
    }

    private static void deleteData(String nameExpression) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        DELETE FROM programming_language
                        WHERE name LIKE ?
                    """)) {
                statement.setString(1, nameExpression);
                int rowsDeleted = statement.executeUpdate();
                System.out.println("Rows deleted: " + rowsDeleted);
            }
        }
    }

}

