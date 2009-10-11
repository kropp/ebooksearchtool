package org.ebooksearchtool.analyzer.network;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Алексей
 */

public class NetworkSocketIO{

    public static void createServer(InetAddress address, int socketNumber){
       try {
            ServerSocket socket = new ServerSocket(socketNumber);
            ServerSocketThread server = new ServerSocketThread(socket);
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(NetworkSocketIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createClient(InetAddress address, int socketNumber){
        try {
            Socket socket = new Socket("localhost", socketNumber);
            ClientSocketThread client = new ClientSocketThread(socket);
            client.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkSocketIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NetworkSocketIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
