package org.ebooksearchtool.analyzer.network;

import java.io.IOException;
import java.net.*;
import org.ebooksearchtool.analyzer.io.Logger;

/**
 * @author Алексей
 */

public class NetworkSocketIO{

    //TODO:Подумать над завершением программы при exception
    public static void createServer(String address, int socketNumber){
       try {
            ServerSocket socket = new ServerSocket(socketNumber);
            ServerSocketThread server = new ServerSocketThread(socket);
            server.start();
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() + " " + address + ":" + socketNumber);
            System.exit(0);
        }
    }

    public static void createClient(String address, int socketNumber, long timeToWait){
//        Object timeout = new Object();
//        while(true){
            try {
                Socket socket = new Socket(address, socketNumber);
                ClientSocketThread client = new ClientSocketThread(socket);
                client.start();
            } catch (UnknownHostException ex) {
                Logger.setToErrorLog(ex.getMessage() + " " + address + ":" + socketNumber);
                System.exit(0);
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage() + " " + address + ":" + socketNumber);
                System.exit(0);
            }
//            try {
//                timeout.wait(timeToWait);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(NetworkSocketIO.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    public static void createUI(){
        UIThread ui = new UIThread();
        ui.start();
    }
}
