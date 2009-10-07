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

    public ClientSocketThread(Socket sSocket){
        mySocket = sSocket;
    }

    @Override
    public synchronized void run(){
        InputStream is;
        OutputStream os;
        try {
            is = mySocket.getInputStream();
            os = mySocket.getOutputStream();
            byte[] bytes = new byte[255];
            StringBuilder buffer = new StringBuilder();

            while(true){
                while(buffer.indexOf(";") == -1){
                    System.in.read(bytes);
                    buffer.append(NetUtils.convertBytesToString(bytes));
                }
                NetUtils.sendMessage(os, buffer.toString().trim());
                buffer = new StringBuilder(NetUtils.reciveMessage(is));
                System.out.print(buffer.toString());
                if(buffer.indexOf("quit") == -1){
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
