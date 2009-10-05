package org.ebooksearchtool.analyzer.network;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Алексей
 */

public class ServerThread extends Thread{

    ServerSocket myServerSocket = null;
    Socket mySocket = null;

    public ServerThread(ServerSocket sSocket){
        myServerSocket = sSocket;
    }

    @Override
    public synchronized void start(){
        try {
            mySocket = myServerSocket.accept();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        InputStream is;
        OutputStream os;
        try {
            is = mySocket.getInputStream();
            os = mySocket.getOutputStream();
            byte[] input = new byte[255];
            StringBuilder str = new StringBuilder();

            while(true){
                is.read(input);
                str.append(convertBytesToString(input));
                System.out.print(str.toString());
                os.write(input, 1 , input.length - 2);
                if(str.indexOf("quit") == -1){
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static void sendString(OutputStream os, String s) throws IOException {
        for(int i = 0; i < s.length(); i++)
        {
          os.write((byte)s.charAt(i));
        }
        os.write('\n');
        os.flush();
    }

    static String convertBytesToString(byte[] b){
        int index = 0;
        StringBuilder str = new StringBuilder();
        while(index < b.length){
            str.append((char) b[index]);
            index++;
        }
        return str.toString();
    }
}
