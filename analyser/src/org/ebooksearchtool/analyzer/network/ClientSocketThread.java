package org.ebooksearchtool.analyzer.network;

import java.util.logging.Level;
import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import org.ebooksearchtool.analyzer.io.Logger;

/**
 * @author Алексей
 */

public class ClientSocketThread extends Thread{

    private static Socket mySocket;
    private static int myPort;
    private static String myHost;
    private static BufferedReader myReader;
    private static BufferedWriter myWriter;
    private static final Object myLock = new Object();

    public ClientSocketThread(Socket sSocket){
        mySocket = sSocket;
        myPort = sSocket.getPort();
        myHost = sSocket.getInetAddress().getHostAddress();
        myReader = null;
        myWriter = null;
    }

    @Override
    public synchronized void run(){
        try{
            try {
                myReader = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                myWriter = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));

                System.out.print(mySocket.getPort());

                while(true){
                    synchronized(myLock){
                        try {
                            myLock.wait();
                        } catch (InterruptedException ex) {
                            Logger.setToErrorLog(ex.getMessage());
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage());
            }finally{
                myReader.close();
                myWriter.close();
                mySocket.close();
            }
        }catch(IOException ex){
            Logger.setToErrorLog(ex.getMessage());
        }
    }

    public static synchronized String sendRequest(String request){
        String message = "";
        try {
            try {
                NetUtils.sendMessage(myWriter, request);
                message = URLDecoder.decode(NetUtils.reciveServerMessage(myReader), "UTF-8");
                //reInitiateSocket();
                System.out.println(message);
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage());
            }finally{
                myWriter.flush();
            }
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage());
        } catch (NullPointerException ex){
            Logger.setToErrorLog("No server connection found. Please chek the server connection.");
        }
        if(message.length() == 0){
            return "Reciving error. Mesage";
        }

        return message;
    }

    private static void reInitiateSocket(){
        try {
            mySocket.close();
            myReader.close();
            myWriter.close();
            mySocket = new Socket(myHost, myPort);
            myReader = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            myWriter = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage());
        }
    }
}
