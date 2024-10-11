package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {

    private static DatabaseManager instance;

    private Connection connection;
    private Statement statement;

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

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public static void menu(Connection connection, Statement statement) throws SQLException {
        System.out.println("MENU");
        System.out.println("Please choose from the menu:");
        System.out.println("1) Create Table \n"+
                "2) Add Value to the Table \n"+
                "3) Update the Table \n"+
                "4) Delete from the Table \n"+
                "5) Display the Table \n" +
                "6) Quit");

        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Please choose an option from the menu");
        int usersChoice = inputScanner.nextInt();

        switch (usersChoice) {
            case 1: createTable(connection, statement);break;
            case 2: addValue(connection, statement);break;
            case 3: updateTable(connection, statement);break;
            case 4: deleteTable(connection, statement);break;
            case 5: displayTable(connection, statement);break;
        }
    }

    public static void createTable(Connection connection, Statement statement) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the table name");
        String tableName = input.nextLine();
        System.out.println("Please enter the columns of the table: ");
        String columnsOfTheTable = input.nextLine();

        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+ tableName + " ("+ columnsOfTheTable;
        statement.executeUpdate(createTableSQL);
        System.out.println("Table is created");


        menu(connection,statement);
    }

    public static void addValue(Connection connection, Statement statement) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the table name");
        String tableName = input.nextLine();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tableName, null);

        List<String> columnNames = new ArrayList<>();
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            columnNames.add(columnName);
        }
        if (columnNames.isEmpty()) {
            System.out.println("Table " + tableName + " does not exist or has no columns.");
            return;
        }
        List<String> values = new ArrayList<>();
        for (String columnName : columnNames) {
            System.out.println("Please enter value for " + columnName + ":");
            String value = input.nextLine();
            values.add(value);
        }

        String columnsJoined = String.join(", ", columnNames);
        String placeholders = String.join(", ", Collections.nCopies(values.size(), "?"));
        String insertSQL = "INSERT INTO " + tableName + " (" + columnsJoined + ") VALUES (" + placeholders + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setString(i + 1, values.get(i));  // Set each value dynamically
            }
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s) into " + tableName + " table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Data inserted");


        menu(connection,statement);

    }

    public static void updateTable(Connection connection, Statement statement) throws SQLException {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Please enter the update command line:");
        String updateSQL = inputScanner.nextLine();

        try {
            statement.executeUpdate(updateSQL);
            System.out.println("The table is updated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        menu(connection,statement);
    }

    public static void deleteTable(Connection connection, Statement statement) throws SQLException {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Please enter the delete command line:");
        String deleteSQL = inputScanner.nextLine();

        try {
            statement.executeUpdate(deleteSQL);
            System.out.println("Data deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        menu(connection,statement);
    }

    public static void displayTable(Connection connection, Statement statement) throws SQLException {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Please enter the table name:");
        String tableName = inputScanner.nextLine();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columnsOfTheTable = metaData.getColumns(null, null, tableName, null);
        List<String> columnNamesList = new ArrayList<>();
        while (columnsOfTheTable.next()) {
            String columnName = columnsOfTheTable.getString("COLUMN_NAME");
            columnNamesList.add(columnName);
        }
        if (columnNamesList.isEmpty()) {
            System.out.println("Table " + tableName + " does not exist or has no columns.");
            return;
        }

        String selectSQL = "SELECT * FROM "+ tableName +";";

        try (ResultSet resultSet = statement.executeQuery(selectSQL)) {
            while (resultSet.next()) {
                for (String columnName : columnNamesList) {
                    String value = resultSet.getString(columnName);
                    System.out.print(columnName + ": " + value + " | ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        menu(connection,statement);
    }







}
