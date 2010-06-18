
package org.ebooksearchtool.analyser_alternate.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private HttpURLConnection mySearchConnection = null;
    private HttpURLConnection myInsertConnection = null;

    public ServerConnector(URL serverURL, URL searchURL, URL insertURL,
            DBConnector modelDBConnector) throws IOException {
        myDBConnector = modelDBConnector;

        myURLConnection = (HttpURLConnection) serverURL.openConnection();
        mySearchConnection = (HttpURLConnection) searchURL.openConnection();
        myInsertConnection = (HttpURLConnection) insertURL.openConnection();

        establishConnections();
    }

    private void establishConnections() {
        Logger logger = Logger.getLogger("main.log");
        try {
            myURLConnection.setRequestMethod("GET");
            mySearchConnection.setRequestMethod("POST");
            myInsertConnection.setRequestMethod("POST");
        } catch (ProtocolException ex) {
            logger.log(Level.SEVERE, "Cannot set GET/POST method" + ex.getMessage());
        }
        try {
            myURLConnection.connect();
            mySearchConnection.setDoOutput(true);
            myInsertConnection.setDoOutput(true);

            mySearchConnection.connect();
            myInsertConnection.connect();
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
        StringBuffer text = new StringBuffer();
        try {
            while(reader.ready()) {
                text.append(reader.readLine());
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read line from server"
                                                            + ex.getMessage());
        }
        try {
            reader.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot close server reader"
                                                            + ex.getMessage());
        }
        String serverVersion = text.substring(text.indexOf("<version>") + 9,
                                text.indexOf("</version>"));
        String serverBuild = text.substring(text.indexOf("<build>") + 7,
                                text.indexOf("</build>"));
        logger.log(Level.INFO, "Server connected. Version: " + serverVersion +
                                    " Build: " + serverBuild);
    }
    
    public void run() {
        Logger logger = Logger.getLogger("main.log");
        ResultSet result = null;
        String author = null;
        try {
            result = myDBConnector.extractData();
            result.next();
            author = result.getString("author");
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Cannot extract data from table. "
                                                            + ex.getMessage());
        }

        OutputStreamWriter outputWriter;
        try {
            outputWriter = new OutputStreamWriter(mySearchConnection.getOutputStream());
            outputWriter.write("xml=");
            outputWriter.write("<request>");
            outputWriter.write("<author>" + author + "</author>");
            outputWriter.write("</request>");
            outputWriter.flush();

            // Get the response
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(mySearchConnection.getInputStream()));
            String line;
            while ((line = serverReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            myURLConnection.disconnect();
            myInsertConnection.disconnect();
            mySearchConnection.disconnect();
        } finally {
            super.finalize();
        }
    }


}
