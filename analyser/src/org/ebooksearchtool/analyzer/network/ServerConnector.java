package org.ebooksearchtool.analyzer.network;

import java.net.MalformedURLException;
import java.util.logging.Level;
import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;
import org.ebooksearchtool.analyzer.utils.BookInfoFormer;

/**
 * @author Алексей
 */

public class ServerConnector extends Thread{

    public static final int GET_REQUEST = 0;
    public static final int INSERT_REQUEST = 1;

    private static URL myInsertURL;
    private static URL myGetURL;
    private static HttpURLConnection myConnection;
    private static final Object myLock = new Object();

    public ServerConnector(String address, int port) throws MalformedURLException{
        myInsertURL = new URL(AnalyzerProperties.getPropertie("default_protocol") +
                "://" + address + ":" + port +
                AnalyzerProperties.getPropertie("server_insert_distanation"));
        myConnection = null;
    }

    @Override
    public synchronized void run(){
        try {
            myConnection = (HttpURLConnection) myInsertURL.openConnection(NetUtils.serverProxyInit());
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);

            //TODO:Доделать после поддержки сервером
            //Server Timeout connection
            System.out.println("Server connected on:");
            System.out.println(myInsertURL);
            System.out.println("");
            Logger.setToLog("Server connected on: " + myInsertURL);
//          establishConnection();

            while(true){
                synchronized(myLock){
                    try {
                        myLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.setToErrorLog(ex.getMessage() + ". ServerConnector thread was interrupted.");
                    }
                }
            }
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() +
                    ". Connection to server failed in client thread initialization.");
        }
}

    public static synchronized String sendRequest(String request, int requestType){
        String message = "";
        try {
            try {
                if(requestType == INSERT_REQUEST){
                    myConnection = (HttpURLConnection) myInsertURL.openConnection(NetUtils.serverProxyInit());
                }else{
                    myConnection = (HttpURLConnection) myGetURL.openConnection(NetUtils.serverProxyInit());
                }
                NetUtils.sendMessage(myConnection, request);
                message = URLDecoder.decode(NetUtils.reciveServerMessage(myConnection), "UTF-8");
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage() + ". Connection to server failed in request sending.");
                throw new NullPointerException(ex.getMessage());
            }
        } catch (NullPointerException ex){
            Logger.setToErrorLog("No server connection found. Please chek the connection." +
                    " Analyzer will try to reconnect.");
            establishConnection();
            message = sendRequest(request, requestType);
        }
        if(message.length() == 0){
            return "Error in reciving response. Message from server is empty.";
        }

        return message;
    }

    private static boolean isConnectionEstablished(){
        String message = ServerConnector.sendRequest
                                (BookInfoFormer.initRequest(), ServerConnector.GET_REQUEST);

        if(NetUtils.serverAnswersAnalyze(message)){
            System.out.println("Server connected on:");
            System.out.println(myInsertURL);
            System.out.println("");
            Logger.setToLog("Server connected on: " + myInsertURL);
            return true;
        }
        return false;
    }

    private static void establishConnection(){
         while(!isConnectionEstablished()){
            synchronized(myLock){
                try {
                    myLock.wait(AnalyzerProperties.getPropertieAsNumber("server_timeout"));
                } catch (InterruptedException ex) {
                    Logger.setToErrorLog(ex.getMessage() + ". ServerConnector thread was interrupted.");
                }
            }
         }
    }
}
