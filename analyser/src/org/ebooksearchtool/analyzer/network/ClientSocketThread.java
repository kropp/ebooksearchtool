package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import org.ebooksearchtool.analyzer.io.Logger;

/**
 * @author Алексей
 */

public class ClientSocketThread extends Thread{

    private Socket mySocket;
    private static BufferedReader myReader;
    private static BufferedWriter myWriter;
    private static final Object myLock = new Object();

    public ClientSocketThread(Socket sSocket){
        mySocket = sSocket;
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
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage());
            }finally{
                myWriter.flush();
            }
        } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage());
        }
        if(message.length() == 0){
            return "Reciving error";
        }

        return message;
    }
}
