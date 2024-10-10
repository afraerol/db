package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String jdbcUrl = "jdbc:mysql://localhost:3306/test-docker";
        String username = "afra";
        String password = "afra";

        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to database");
            statement = connection.createStatement();

            // columns: id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), surname VARCHAR(50), depname VARCHAR(50));
            // String updateSQL = UPDATE employee SET depname='product' WHERE name='Efe';
            // delete: DELETE FROM employee WHERE id = 4;

            System.out.println("MENU");
            System.out.println("Please choose from the menu:");
            System.out.println("1) Create Table \n"+
                    "2) Add Value to the Table \n"+
                    "3) Update the Table \n"+
                    "4) Delete from the Table \n"+
                    "5) Display the Table \n" +
                    "6) Quit");
            Scanner input = new Scanner(System.in);
            int option = input.nextInt();

            menu(connection,statement,option);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            System.out.println("Please enter the  name");
            String name = input.nextLine();
            System.out.println("Please enter the surname");
            String surname = input.nextLine();
            System.out.println("Please enter the department name");
            String depname = input.nextLine();

        String insertSQL = "INSERT INTO employee (name, surname, depname) VALUES (?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, depname);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s) into employee table.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        // statement.executeUpdate(insertSQL);
        /*String[] emplist = {
                "INSERT INTO employee (name, surname, depname) VALUES ('Uygar', 'İşiçelik', 'software')",
                "INSERT INTO employee (name, surname, depname) VALUES ('Sarper', 'Kumcu', 'software')",
                "INSERT INTO employee (name, surname, depname) VALUES ('Cansu', 'Bozkurt', 'software')"
        };

        for(String emp : emplist) {
            try {
                statement.executeUpdate(emp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }*/
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
        Scanner input = new Scanner(System.in);
        String selectSQL2 = "SELECT * FROM employee";

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(selectSQL2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String depname = resultSet.getString("depname");

            System.out.println("ID: " + id + ", Name: " + name + ", Surname: " + surname + ", Depname: " + depname);
        }
        System.out.println("Please choose another option from the menu");
        int option = input.nextInt();
        menu(connection,statement,option);
    }
}