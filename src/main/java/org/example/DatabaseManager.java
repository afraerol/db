package org.example;

import java.sql.*;
import java.util.Scanner;

public class DatabaseManager {

    private static DatabaseManager instance;

    public Connection connection;
    public Statement statement;

    String jdbcUrl = "jdbc:mysql://localhost:3306/test-docker";
    String username = "afra";
    String password = "afra";


    private DatabaseManager() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(jdbcUrl, username, password);
        statement = connection.createStatement();
    }


    public static DatabaseManager getInstance() throws SQLException, ClassNotFoundException {
        try {
            if (instance == null) {
                instance = new DatabaseManager();
            }
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }







}
