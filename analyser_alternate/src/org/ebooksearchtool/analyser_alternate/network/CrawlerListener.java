
package org.ebooksearchtool.analyser_alternate.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ebooksearchtool.analyser_alternate.dbconnection.DBConnector;
import org.ebooksearchtool.analyser_alternate.xml.CrawlerMessageParser;

/**
 *
 * @author catherine_tuzova
 *
 * class for connection to crawler
 * it recieve crawler messages and
 * commit them to database
 */
public class CrawlerListener implements Runnable{

    private int myCrawlerPort;
    private DBConnector myDBConnector;
    private CrawlerMessageParser myMessageParser = new CrawlerMessageParser();

    public CrawlerListener(int port, DBConnector dbConnector ) {
        myCrawlerPort = port;
        myDBConnector = dbConnector;
    }

    /**
     *
     * listen crawler on port from configs
     * recieve incoming messages
     */
    public void run() {
        Logger logger = Logger.getLogger("main.log");
        logger.log(Level.INFO,"Start listening crawler on port " + myCrawlerPort);
        try {
            // open crawler connection
            ServerSocket socket = new ServerSocket(myCrawlerPort);
            Socket crawlerSocket = socket.accept();
            logger.log(Level.INFO,"Crawler connected.");

            // read crawler message from socket
            BufferedReader input = new BufferedReader(
                        new InputStreamReader(crawlerSocket.getInputStream()));
            while(true) {
                StringBuffer strBuffer = new StringBuffer();
                while(strBuffer.indexOf("</root>") == -1) {
                    try {
                        strBuffer.append(input.readLine());
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, ex.getMessage());
                        socket.close();
                        crawlerSocket.close();
                        input.close();
                    }
                }
                myMessageParser.setMessage(strBuffer);
                ArrayList<String> params = new ArrayList<String>();
                params.add(myMessageParser.getLink());
                params.add(myMessageParser.getPage());
                try {
                    myDBConnector.insertData(params);
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE,"Cannot insert data to database. "
                            + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }

    }

}
