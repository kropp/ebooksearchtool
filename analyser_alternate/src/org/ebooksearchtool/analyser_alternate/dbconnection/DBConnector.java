
package org.ebooksearchtool.analyser_alternate.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ebooksearchtool.analyser_alternate.Main;

/**
 *
 * @author catherine_tuzova
 *
 * creates connection to database
 */
public class DBConnector {

    private Connection myDBConnection = null;

    private String myTableName;
    private PreparedStatement mySelectStatement;
    private PreparedStatement myInsertStatement;
    private PreparedStatement myDeleteStatement;
    /**
     *
     * @param insert The string using to insert values to db
     * #example# insert = "(book_link, page) VALUES (?, ?)"
     * @return Connection to database
     * @throws SQLException if can't create database connection
     */
    public DBConnector(String tableName, String insert) throws SQLException {
        Properties properties = Main.loadProperties();
        final String dbHost = properties.getProperty("db_host");
        final String dbName = properties.getProperty("db_name");
        final String userName = properties.getProperty("user_name");
        final String userPassword = properties.getProperty("user_password");

        myTableName = tableName;
        StringBuffer url = new StringBuffer("jdbc:mysql://");
        url.append(dbHost);
        url.append("/");
        url.append(dbName);
        myDBConnection = DriverManager.getConnection(url.toString(),
                                                    userName, userPassword);
        String insertString = "INSERT INTO " +  myTableName + " " + insert;
        String selectString = "SELECT *  FROM " +  myTableName + " ORDER BY id LIMIT 1;";
        String deleteString = "DELETE  FROM " +  myTableName + " ORDER BY id LIMIT 1;";

        mySelectStatement = myDBConnection.prepareStatement(selectString);
        myDeleteStatement = myDBConnection.prepareStatement(deleteString);
        myInsertStatement = myDBConnection.prepareStatement(insertString);
    }

    /**
     *
     * @param params to insert
     * @throws SQLException if can't insert data
     */
    public void insertData(ArrayList <String> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            myInsertStatement.setString(i+1, params.get(i));
        }
        myInsertStatement.executeUpdate();
    }

    /**
     *
     * @return data from db as ResultSet
     * @throws SQLException if can't extract data
     */
    public ResultSet extractData() throws SQLException {
        // prepare select and delete operations with database
        ResultSet result = mySelectStatement.executeQuery();
        
        myDeleteStatement.executeUpdate();
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            myDeleteStatement.close();
            myInsertStatement.close();
            mySelectStatement.close();
            myDBConnection.close();          // close connection
        } finally {
            super.finalize();
        }

    }

}
