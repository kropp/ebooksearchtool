package org.ebooksearchtool.analyzer.network;

import java.net.URL;
import java.io.*;

/**
 * @author Алексей
 */

public class NetworkURLIO {
     public static void createServer(URL url) throws IOException{
        ServerURLThread server = new ServerURLThread(url);
        server.start();
        //Может быть не надо, потестить.
//        try {
//            server.join();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(NetworkSocketIO.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static void createClient(URL url) throws IOException{
        ClientURLThread client = new ClientURLThread(url);
        client.start();
        //Может быть не надо, потестить.
//        try {
//            client.join();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(NetworkSocketIO.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
