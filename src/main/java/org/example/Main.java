package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        DatabaseManager dbInstance = DatabaseManager.getInstance();
        System.out.println("Connected to database");

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

            dbInstance.menu(dbInstance.connection,dbInstance.statement,option);


    }





}