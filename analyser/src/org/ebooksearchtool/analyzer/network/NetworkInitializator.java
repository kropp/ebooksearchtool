package org.ebooksearchtool.analyzer.network;

import java.io.IOException;
import java.net.*;
import org.ebooksearchtool.analyzer.io.Logger;

/**
 * @author Алексей
 */

public class NetworkInitializator{

    //TODO:Подумать над завершением программы при exception. Сделать нормальный интервал попытки перезапуска.
    public static void createCrawlerConnector(String address, int socketNumber){
       try {
            ServerSocket socket = new ServerSocket(socketNumber);
            CrawlerConnector server = new CrawlerConnector(socket);
            server.start();
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". Can't connect to crawler."
                    + " " + address + ":" + socketNumber);
            //createCrawlerConnector(address, socketNumber);
            //System.exit(0);
        }
    }

    public static void createServerConnector(String address, int socketNumber, long timeToWait){
//        Object timeout = new Object();
//        while(true){
            try {
                ServerConnector client = new ServerConnector(address, socketNumber);
                client.start();
            } catch (MalformedURLException ex) {
                Logger.setToErrorLog(ex.getMessage() + ". Can't connect to server."
                        + " " + address + ":" + socketNumber);
                createServerConnector(address, socketNumber, timeToWait);
                //System.exit(0);
            }// catch (IOException ex) {
//                Logger.setToErrorLog(ex.getMessage() + " " + address + ":" + socketNumber);
//                System.exit(0);
//            }
//            try {
//                timeout.wait(timeToWait);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(NetworkInitializator.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    public static void createUI(){
        UIThread ui = new UIThread();
        ui.start();
    }
}
