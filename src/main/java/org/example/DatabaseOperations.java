package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface DatabaseOperations {
    void createTable(Connection connection, Statement statement) throws SQLException;
    void insertValue(Connection connection, Statement statement) throws SQLException;
    void updateTable(Connection connection, Statement statement) throws SQLException;
    void deleteTable(Connection connection, Statement statement) throws SQLException;
    void displayTable(Connection connection, Statement statement) throws SQLException;
}
