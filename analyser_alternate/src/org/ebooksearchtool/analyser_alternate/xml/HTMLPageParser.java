
package org.ebooksearchtool.analyser_alternate.xml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
//import net.htmlparser.jericho.*;
import java.util.logging.Logger;
import org.ebooksearchtool.analyser_alternate.dbconnection.DBConnector;
/**
 *
 * @author catherine_tuzova
 *
 * parse html page from crawler,
 * extracts information about author, title...
 */
public class HTMLPageParser implements Runnable {
    private DBConnector myDBConnector;

    public HTMLPageParser(DBConnector dbConnector) {
        myDBConnector = dbConnector;
    }

    public void run() {
        Logger logger = Logger.getLogger("main.log");
        logger.log(Level.INFO, "Start html parser");
        String selectString = "SELECT *  FROM Message ORDER BY id LIMIT 1;";
        String deleteString = "DELETE  FROM Message ORDER BY id LIMIT 1;";

        while(true) {
            try {
                // gets element from db
                ResultSet resultSet = myDBConnector.extractData();
                resultSet.next();
                System.out.println(resultSet.getString("book_link"));
                System.out.println(resultSet.getString("page"));
            } catch (SQLException ex) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex1) {
                    logger.log(Level.SEVERE, "Thread sleep has been interrupted");
                }
                logger.log(Level.INFO, "Error extraction data operation. "
                                                            + ex.getMessage());
            }
        }
        
    }
}
