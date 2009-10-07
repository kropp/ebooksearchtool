package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Алексей
 */

public class ServerSocketThread extends Thread{

    private ServerSocket myServerSocket = null;
    private Socket mySocket = null;

    public ServerSocketThread(ServerSocket sSocket){
        myServerSocket = sSocket;
    }

    @Override
    public synchronized void run(){
        try {
            mySocket = myServerSocket.accept();
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        InputStream is;
        OutputStream os;
        try {
            is = mySocket.getInputStream();
            os = mySocket.getOutputStream();
            String buffer = "";

            while(true){
                buffer = NetUtils.reciveMessage(is);
                if(buffer.length() > 3){
                    buffer = buffer.substring(1, buffer.length() - 2);
                }
                os.write(NetUtils.convertToByteArray(buffer));
                if(buffer.indexOf("quit") == -1){
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
}
