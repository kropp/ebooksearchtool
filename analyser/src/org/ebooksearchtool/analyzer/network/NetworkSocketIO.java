package org.ebooksearchtool.analyzer.network;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Алексей
 */

public class NetworkSocketIO{

    public static void createServer(InetAddress address, int socketNumber) throws IOException{
        ServerSocket socket = new ServerSocket(socketNumber);
        ServerThread server = new ServerThread(socket);
        server.start();
        //Может быть не надо, потестить.
        try {
            server.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(NetworkSocketIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
