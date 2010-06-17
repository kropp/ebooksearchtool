
package org.ebooksearchtool.analyser_alternate.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ebooksearchtool.analyser_alternate.dbconnection.DBConnector;

/**
 *
 * @author catherine_tuzova
 */
public class ServerConnector implements Runnable{

    private DBConnector myDBConnector = null;
    private HttpURLConnection myURLConnection = null;

    public ServerConnector(URL serverURL, DBConnector modelDBConnector)
                                                            throws IOException {
        myDBConnector = modelDBConnector;

        myURLConnection = (HttpURLConnection) serverURL.openConnection();
    }

    public void run() {
        Logger logger = Logger.getLogger("main.log");
        ResultSet result = null;
        try {
            result = myDBConnector.extractData();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Cannot extract data from table"
                                                            + ex.getMessage());
        }
        try {
            //myURLConnection.setRequestProperty("Content-type",
            //        "application/x-www-form-urlencoded; charset=UTF-8");
            myURLConnection.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            logger.log(Level.SEVERE, "Cannot set GET method" + ex.getMessage());
        }
        try {
            myURLConnection.connect();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot connect to server" + ex.getMessage());
        }
        InputStream in = null;
        try {
            in = myURLConnection.getInputStream();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot get input from server"
                                                            + ex.getMessage());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String text = null;
        try {
            text = reader.readLine();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read line from server"
                                                            + ex.getMessage());
        }
        System.out.println(text);

    }

    @Override
    protected void finalize() throws Throwable {
        myURLConnection.disconnect();
        super.finalize();
    }


}
