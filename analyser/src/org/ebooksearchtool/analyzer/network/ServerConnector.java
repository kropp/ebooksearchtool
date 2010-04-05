package org.ebooksearchtool.analyzer.network;

import java.net.MalformedURLException;
import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;
import org.ebooksearchtool.analyzer.utils.RequestFormer;

/**
 * @author Aleksey Podolskiy
 */
public class ServerConnector extends Thread {

    public static final int GET_REQUEST = 0;
    public static final int INSERT_REQUEST = 1;
    public static final int INIT_REQUEST = 2;
    private static URL myInsertURL;
    private static URL myGetURL;
    private static URL myInitURL;
    private static HttpURLConnection myConnection;
    private static final Object myLock = new Object();

    public ServerConnector(String address, int port) throws MalformedURLException {
        myInsertURL = new URL(AnalyzerProperties.getPropertie("default_protocol") +
                "://" + address + ":" + port +
                AnalyzerProperties.getPropertie("server_insert_distanation"));
        myGetURL = new URL(AnalyzerProperties.getPropertie("default_protocol") +
                "://" + address + ":" + port +
                AnalyzerProperties.getPropertie("server_get_distanation"));
        myInitURL = new URL(AnalyzerProperties.getPropertie("default_protocol") +
                "://" + address + ":" + port +
                AnalyzerProperties.getPropertie("server_init_distanation"));
        myConnection = null;
    }

    @Override
    public synchronized void run() {
//        try {
        // myConnection = (HttpURLConnection) myInitURL.openConnection(
        //         NetUtils.serverProxyInit());
        //myConnection.setDoInput(true);
        // myConnection.setDoOutput(true);

        sendRequest(RequestFormer.getInitRequest(), INIT_REQUEST);

        while (true) {
            synchronized (myLock) {
                try {
                    myLock.wait();
                } catch (InterruptedException ex) {
                    Logger.setToErrorLog(ex.getMessage() + ". " +
                            "ServerConnector thread was interrupted.");
                }
            }
        }
//        } catch (IOException ex) {
//            Logger.setToErrorLog(ex.getMessage() +
//                    ". Connection to server failed in client thread " +
//                    "initialization.");
//        }
    }

    /**
     * Send request to server. Type of request may be:
     * GET_REQUEST = 0;
     * INSERT_REQUEST = 1;
     * INIT_REQUEST = 2;
     */
    public static synchronized String sendRequest(String request,
            int requestType) {
        String message = "";
        try {
            if (requestType == INSERT_REQUEST) {
                myConnection = (HttpURLConnection) myInsertURL.openConnection(
                        NetUtils.serverProxyInit());
                myConnection.setDoInput(true);
                myConnection.setDoOutput(true);
                NetUtils.sendMessage(myConnection, request, "POST");
                message = URLDecoder.decode(
                        NetUtils.reciveServerMessage(myConnection), "UTF-8");
            } else {
                if (requestType == GET_REQUEST) {
                    myConnection = (HttpURLConnection) myGetURL.openConnection(
                            NetUtils.serverProxyInit());
                    myConnection.setDoInput(true);
                    myConnection.setDoOutput(true);
                    NetUtils.sendMessage(myConnection, request, "POST");
                    message = URLDecoder.decode(
                            NetUtils.reciveServerMessage(myConnection), "UTF-8");
                } else {
                    message = connect();
                    //TODO:Look how to work with timeouts
//                        synchronized(myLock){
//                            try {
//                                myLock.wait(AnalyzerProperties.getPropertieAsNumber(
//                                        "server_timeout"));
//                            } catch (InterruptedException ex) {
//                                Logger.setToErrorLog(ex.getMessage() + ". " +
//                                        "ServerConnector thread was interrupted.");
//                            }
//                        }
                    }
            }
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". No server connection" +
                    " found. Please chek the connection." +
                    " Analyzer will try to reconnect.");
            connect();
            message = sendRequest(request, requestType);
        }
        if (message.length() == 0) {
            return "Error in reciving response. Message from server is empty.";
        }

        return message;
    }

    private static String connect() {
        boolean isConnectedFlag = false;
        String message = "";
        while (!isConnectedFlag) {
            try {
                System.out.println("Analyzer try to connect to server." +
                        " Please wait.");
                myConnection = (HttpURLConnection) myInitURL.openConnection(
                        NetUtils.serverProxyInit());
                myConnection.setDoInput(true);
                myConnection.setDoOutput(true);
                NetUtils.sendMessage(myConnection,
                        RequestFormer.getInitRequest(), "GET");
                message = URLDecoder.decode(
                        NetUtils.reciveServerMessage(myConnection),
                        "UTF-8");
                if (NetUtils.serverConnectionAnswersAnalyze(message)) {
                    isConnectedFlag = true;
                    System.out.println("Server connected on:");
                    System.out.println(myInitURL);
                    System.out.println("");
                    Logger.setToLog("Server connected on: " + myInitURL);
                }
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage() + ". No server " +
                        "connection found. Please chek the connection." +
                        " Analyzer will try to reconnect.");
            }
        }
        return message;
    }
}
