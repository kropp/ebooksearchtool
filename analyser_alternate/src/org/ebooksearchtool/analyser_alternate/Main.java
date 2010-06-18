
package org.ebooksearchtool.analyser_alternate;

import java.net.MalformedURLException;
import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ebooksearchtool.analyser_alternate.dbconnection.DBConnector;
import org.ebooksearchtool.analyser_alternate.network.CrawlerListener;
import org.ebooksearchtool.analyser_alternate.network.ServerConnector;
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
        {"crawler_port", "9999"},
        {"db_host","localhost"},
        {"db_name","analyserdb"},
        {"user_name", "catherine"},
        {"user_password",""},
        {"raw_data_table_name", "Message"},
        {"model_table_name", "Model"},
        {"protocol", "http"},
        {"server_host", "127.0.0.1"},
        {"server_port", "8000"},
        {"server_insert", "/data/insert"},
        {"server_init", "/data"},
        {"server_search", "/data/search"}
    };

    private static final Logger ourLogger = Logger.getLogger("main.log");
    
    public static void main(String[] args) {
        ourLogger.log(Level.INFO,"Start analyser.");

        // load properties
        Properties properties = loadProperties();

        // create connector to database storing raw data
        String rawDataTableName = properties.getProperty("raw_data_table_name");

        DBConnector messageConnector = null;
        try {
            messageConnector = new DBConnector(rawDataTableName,
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

        // create connector to database storing processed data
        String modelTableName = properties.getProperty("model_table_name");
        DBConnector modelConnector = null;
        try {
            modelConnector = new DBConnector(modelTableName,
                                    "(title, author) VALUES (?, ?)");
        } catch (SQLException ex) {
            ourLogger.log(Level.SEVERE,
                    "Cannot establish connection to database" + ex.getMessage());
            return;
        }
        // create parser for incoming messages
       /*HTMLPageParser parser = new HTMLPageParser(messageConnector, modelConnector);
        Thread parserThread = new Thread(parser);
        parserThread.start();*/

        // create server connection

        String protocol = properties.getProperty("protocol");
        String serverHost = properties.getProperty("server_host");
        String serverPort = properties.getProperty("server_port");
        String init = properties.getProperty("server_init");
        String search = properties.getProperty("server_search");
        String insert = properties.getProperty("server_insert");
        URL serverURL = null;
        URL serverSearch = null;
        URL serverInsert = null;
        try {
            serverURL = new URL(protocol + "://" + serverHost + ":" 
                                                         + serverPort + init);
            serverSearch = new URL(protocol + "://" + serverHost + ":"
                                                         + serverPort + search);
            serverInsert = new URL(protocol + "://" + serverHost + ":"
                                                         + serverPort + insert);
        } catch (MalformedURLException ex) {
            ourLogger.log(Level.SEVERE, "Bad server URL" + ex.getMessage());
        }
        ServerConnector serverConnector = null;
        try {
            serverConnector = new ServerConnector(serverURL, serverSearch,
                                                serverInsert, modelConnector);
        } catch (IOException ex) {
            ourLogger.log(Level.SEVERE, "Cannot open server connection"
                                                        + ex.getMessage());
        }
        Thread serverThread = new Thread(serverConnector);
        serverThread.start();
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
