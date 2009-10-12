package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Алексей
 */

public class ClientSocketThread extends Thread{

    private Socket mySocket;
    private static BufferedReader myReader;
    private static BufferedWriter myWriter;

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
//                byte[] bytes = new byte[255];
//                StringBuilder buffer = new StringBuilder();

                System.out.print(mySocket.getPort());

                while(true){
//                    while(buffer.indexOf(";") == -1){
//                        System.in.read(bytes);
//                        buffer.append(NetUtils.convertBytesToString(bytes));
//                    }
//                    NetUtils.sendMessage(myWriter, buffer.toString().trim());
//                    buffer = new StringBuilder(NetUtils.reciveMessage(myReader));
//                    System.out.print(buffer.toString());
//                    if(buffer.indexOf("quit") != -1){
//                        break;
//                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                myReader.close();
                myWriter.close();
                mySocket.close();
            }
        }catch(IOException ex){
            Logger.getLogger(ClientSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized String sendRequest(String request){
        String message = "";
        try {
            try {
                NetUtils.sendMessage(myWriter, request);
                message = NetUtils.reciveMessage(myReader);
            } catch (IOException ex) {
                Logger.getLogger(ClientSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                myWriter.flush();
            }
        } catch (IOException ex) {
                Logger.getLogger(ClientSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(message.length() == 0){
            return "Reciving error";
        }
        return message;
    }
}
