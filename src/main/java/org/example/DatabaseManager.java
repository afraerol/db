package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public static void menu(Connection connection, Statement statement, int option) throws SQLException {


        switch (option) {
            case 1: createTable(connection, statement);break;
            case 2: addValue(connection, statement);break;
            case 3: updateTable(connection, statement);break;
            case 4: deleteTable(connection, statement);break;
            case 5: displayTable(connection, statement);break;

        }

    }

    public static void createTable(Connection connection, Statement statement) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the name of the table: ");
        String tablename = input.nextLine();
        System.out.println("Please enter the columns of the table: ");
        String columns = input.nextLine();


        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+ tablename + " ("+ columns;

        statement.executeUpdate(createTableSQL);
        System.out.println("Table is created");
        System.out.println("Please choose another option from the menu");
        int option = input.nextInt();
        menu(connection,statement,option);

    }
    public static void addValue(Connection connection, Statement statement) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the table name");
        String tname = input.nextLine();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tname, null);

        List<String> columnNames = new ArrayList<>();
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            columnNames.add(columnName);
        }
        if (columnNames.isEmpty()) {
            System.out.println("Table " + tname + " does not exist or has no columns.");
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
        String insertSQL = "INSERT INTO " + tname + " (" + columnsJoined + ") VALUES (" + placeholders + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setString(i + 1, values.get(i));  // Set each value dynamically
            }
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s) into " + tname + " table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Data inserted");

        System.out.println("Please choose another option from the menu");
        int option = input.nextInt();
        menu(connection,statement,option);

    }
    public static void updateTable(Connection connection, Statement statement) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the update command line:");
        String updateSQL = input.nextLine();

        try {
            statement.executeUpdate(updateSQL);
            System.out.println("The table is updated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Please choose another option from the menu");
        int option = input.nextInt();
        menu(connection,statement,option);


    }
    public static void deleteTable(Connection connection, Statement statement) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the delete command line:");
        String deleteSQL = input.nextLine();
        try {
            statement.executeUpdate(deleteSQL);
            System.out.println("Data deleted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Please choose another option from the menu");
        int option = input.nextInt();
        menu(connection,statement,option);

    }
    public static void displayTable(Connection connection, Statement statement) throws SQLException {
        System.out.println("Please enter the table name:");
        Scanner input = new Scanner(System.in);
        String tname = input.nextLine();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tname, null);
        List<String> columnNames = new ArrayList<>();
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            columnNames.add(columnName);
        }
        if (columnNames.isEmpty()) {
            System.out.println("Table " + tname + " does not exist or has no columns.");
            return;
        }

        String selectSQL = "SELECT * FROM "+ tname+";";

        try (ResultSet resultSet = statement.executeQuery(selectSQL)) {
            while (resultSet.next()) {
                // Step 5: Dynamically retrieve and display the values for each column
                for (String columnName : columnNames) {
                    String value = resultSet.getString(columnName);
                    System.out.print(columnName + ": " + value + " | ");
                }
                System.out.println(); // Move to the next line after each row
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Please choose another option from the menu");
        int option = input.nextInt();
        menu(connection,statement,option);
    }







}
