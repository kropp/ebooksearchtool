package org.ebooksearchtool.client.connection;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 22.09.2009
 * Time: 20:38:21
 * To change this template use File | Settings | File Templates.
 */
public class Connector{
	
    URL Url;
    URLConnection connection;
    String myIP;
    int myPort;

    public Connector(String adress, String IP, int port) throws IOException {
        Url = new URL(adress);
        myIP = IP;
        myPort = port;
    }

    public void getFileFromURL(String fileName) {
        
        try {
        	System.out.println("C1 ANS");
            connection = Url.openConnection();
            System.out.println("C1 ANS O");
            PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            System.out.println("C1 ANS PW");
            int i = 0;
            int end = connection.getContentLength();
            char ch;
            while (i!=end){
            	
                ch = (char)connection.getInputStream().read();
            	pw1.print(ch);
                ++i;
                
            }
            pw1.close();
        } catch (IOException e) {
            try {

System.out.println("C2 ANS");
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(myIP, myPort)));
                System.out.println("C2 O");                
                PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
                System.out.println("C2 PW");
                //File outFile = new File(fileName);
                //FileOutputStream outStream = new FileOutputStream(outFile);
                
                int i = 0;
                while (i != connection.getContentLength() )
               {
                	//outStream.write(connection.getInputStream().read());
                    pw1.print((char)connection.getInputStream().read());
                    ++i;
                    System.out.println(i);
                }
                pw1.close();
     
            	
            	            } catch (IOException e1) {
            	System.out.println("connection error");
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128)));



    }
    
public void getBookFromURL(String fileName) {
        
        try {
        	System.out.println("C1 FILE");
            connection = Url.openConnection();
            System.out.println("C1 FILE O");
            File outFile = new File(fileName);
            System.out.println("C1 FILE OS");
            FileOutputStream outStream = new FileOutputStream(outFile);
            System.out.println("C1 FILE OS of");
            InputStream IS = connection.getInputStream();
            System.out.println("C1 FILE IS");

            int i = 0;
            System.out.println("C1 FILE down");
            while (i != -1)
            {
              	i = IS.read();
               	outStream.write(i);
                    //pw1.print((char)connection.getInputStream().read());
               	
            }
            outStream.close();
            
        } catch (IOException e) {
            try {
            	System.out.println("C2 C FILE");
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(myIP, myPort)));
                
                //PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
                System.out.println("C2 OF File");
                File outFile = new File(fileName);
                System.out.println("C2 FILE O");
                FileOutputStream outStream = new FileOutputStream(outFile);
                System.out.println("C2 FILE OS");
                InputStream IS = connection.getInputStream();
                System.out.println("C2 IS FILE");
                int i = 0;
                System.out.println("C1 FILE Down");
                while (i != -1)
               {
                	i = IS.read();
                	outStream.write(i);
                    //pw1.print((char)connection.getInputStream().read());
                	
                }
                outStream.close();
     
            	
            	            } catch (IOException e1) {
            	System.out.println("connection error");
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128)));



    }

}
