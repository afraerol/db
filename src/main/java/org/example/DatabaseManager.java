package org.example;

import java.sql.*;
import java.util.Scanner;

public class DatabaseManager {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/test-docker";
        String username = "afra";
        String password = "afra";

        try {
            // Getting the Singleton instance
            DatabaseManager dbInstance = DatabaseManager.getInstance();

            // Now use the singleton instance to perform operations
            Scanner input = new Scanner(System.in);
            System.out.println("MENU");
            System.out.println("Please choose from the menu:");
            System.out.println("1) Create Table \n"+
                    "2) Add Value to the Table \n"+
                    "3) Update the Table \n"+
                    "4) Delete from the Table \n"+
                    "5) Display the Table \n" +
                    "6) Quit");

            int option = input.nextInt();
            dbInstance.menu(option); // Call the method through the Singleton instance

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }





    }

    // Singleton instance
    private static DatabaseManager instance;

    private Connection connection;
    private Statement statement;

    private String jdbcUrl = "jdbc:mysql://localhost:3306/test-docker";
    private String username = "afra";
    private String password = "afra";

    // Private constructor
    private DatabaseManager() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(jdbcUrl, username, password);
        statement = connection.createStatement();
    }

    // Public method to get the singleton instance
    public static DatabaseManager getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Method to create table
    public void createTable() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the name of the table: ");
        String tablename = input.nextLine();
        System.out.println("Please enter the columns of the table: ");
        String columns = input.nextLine();

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tablename + " (" + columns + ")";
        statement.executeUpdate(createTableSQL);
        System.out.println("Table created.");
    }

    // Method to add values
    public void addValue() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the name:");
        String name = input.nextLine();
        System.out.println("Please enter the surname:");
        String surname = input.nextLine();
        System.out.println("Please enter the department name:");
        String depname = input.nextLine();

        String insertSQL = "INSERT INTO employee (name, surname, depname) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, surname);
        preparedStatement.setString(3, depname);

        int rowsAffected = preparedStatement.executeUpdate();
        System.out.println("Inserted " + rowsAffected + " row(s).");
    }

    public static void menu(int option) throws SQLException {


        switch (option) {
            case 1: createTable(connection, statement);break;
            case 2: addValue(connection, statement);break;
            case 3: updateTable(connection, statement);break;
            case 4: deleteTable(connection, statement);break;
            case 5: displayTable(connection, statement);break;

        }

    }

    // Other methods like updateTable, deleteTable, displayTable can be added similarly
}
