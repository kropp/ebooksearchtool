package org.ebooksearchtool.client.connection;

import org.ebooksearchtool.client.view.Viewer;

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
            connection = Url.openConnection();
            PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            
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

                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(myIP, myPort)));
                
                PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));

                //File outFile = new File(fileName);
                //FileOutputStream outStream = new FileOutputStream(outFile);
                
                int i = 0;
                while (i != connection.getContentLength() )
               {
                	//outStream.write(connection.getInputStream().read());
                    pw1.print((char)connection.getInputStream().read());
                    ++i;
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
            connection = Url.openConnection();
            PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            
            int i = 0;
            int end = connection.getContentLength();
            char ch;
            while (i!=end/*connection.getInputStream().available()/*i != connection.getContentLength()*/){
            	
                ch = (char)connection.getInputStream().read();
            	pw1.print(ch);
                ++i;
                
            }
            pw1.close();
        } catch (IOException e) {
            try {

                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(myIP, myPort)));
                
                //PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));

                File outFile = new File(fileName);
                FileOutputStream outStream = new FileOutputStream(outFile);
                InputStream IS = connection.getInputStream();
                
                int i = 0;
                while (i != -1)
               {
                	System.out.println(i);
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
