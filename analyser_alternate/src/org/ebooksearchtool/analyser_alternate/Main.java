
package org.ebooksearchtool.analyser_alternate;

import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ebooksearchtool.analyser_alternate.dbconnection.DBConnector;
import org.ebooksearchtool.analyser_alternate.network.CrawlerListener;
import org.ebooksearchtool.analyser_alternate.xml.HTMLPageParser;



/**
 *
 * @author catherine_tuzova
 *
 * main class contents main method
 * load properties, configure log,
 * creates connection to database
 */
public class Main {
    public static final File PROPERTIES_FILE = new File("analyser.properties");

    // default properties for programm, using if PROPERTIES_FILE broken
    private static final String[][] ourDefaultProperties = {
        {"path_to_found", "./found_books/found_log.xml.0"},
        {"crawler_port", "9999"},
        {"db_host","localhost"},
        {"db_name","analyserdb"},
        {"user_name", "catherine"},
        {"user_password",""}
    };

    private static final Logger ourLogger = Logger.getLogger("main.log");
    
    public static void main(String[] args) {
        ourLogger.log(Level.INFO,"Start analyser.");

        // load properties
        Properties properties = loadProperties();

        // create connector to database storing raw data
        DBConnector messageConnector = null;
        try {
            messageConnector = new DBConnector("Message",
                                    "(book_link, page) VALUES (?, ?)");
        } catch (SQLException ex) {
            ourLogger.log(Level.SEVERE,
                    "Cannot establish connection to database" + ex.getMessage());
            return;
        }
        
        // listen crawler
        final String crawlerPort = properties.getProperty("crawler_port");
        CrawlerListener crawlerListener =
                            new CrawlerListener(Integer.parseInt(crawlerPort),
                                                                messageConnector);
        Thread crawlerThread = new Thread(crawlerListener);
        crawlerThread.start();

        // create parser for incoming messages
        HTMLPageParser parser = new HTMLPageParser(messageConnector);
        Thread parserThread = new Thread(parser);
        parserThread.start();

    }

    /**
     *
     * @return loaded from file properties
     * if file is broken return default Props
     */
    public static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE)); // Load props from the file
        } catch (IOException ex) {
            for (String[] property : ourDefaultProperties) {
                properties.setProperty(property[0], property[1]);
            }
            ourLogger.log(Level.WARNING,"Cannot open properties file "
                    + PROPERTIES_FILE + ", using default values");
            ourLogger.log(Level.WARNING,"Got exception" + ex.getMessage());
        }
        return properties;
    }

}
