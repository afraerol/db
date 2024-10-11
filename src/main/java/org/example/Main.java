package org.example;

import java.sql.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        DatabaseManager dbInstance = DatabaseManager.getInstance();
        System.out.println("Connected to database");

        // columns: id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), surname VARCHAR(50), depname VARCHAR(50));
        // updateSQL: UPDATE employee SET depname='product' WHERE name='Efe';
        // delete: DELETE FROM employee WHERE id = 4;

        dbInstance.menu(dbInstance.getConnection(), dbInstance.getStatement());

    }
}