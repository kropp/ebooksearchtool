package org.ebooksearchtool.client.connection;

import org.ebooksearchtool.client.view.Viewer;

import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public Connector(String adress) throws IOException {
        Url = new URL(adress);
    }

    public void getFileFromURL(String fileName) {
        
        try {
            System.out.println("C1");
            connection = Url.openConnection();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            System.out.println("start download");
            System.out.println(Url.getFile() + "  " + Url.getProtocol() + "  " + connection.getContentEncoding() + "  " + connection.getInputStream().available());
            int i = 0;
            int end = connection.getInputStream().available();
            char ch;
            while (i!=end/*connection.getInputStream().available()/*i != connection.getContentLength()*/){
            	
                ch = (char)connection.getInputStream().read();
            	pw.print(ch);
                ++i;
                
                System.out.println("D "+i);
            }
            pw.close();
            System.out.println("finish download");
        } catch (IOException e) {
            try {

            	System.out.println("C2");
                String IP = "192.168.0.2";
                int port = 3128;
                //viewer.showProxyDialog(IP, port);
                
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(IP, port)));
                
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));

                int i = 0;
                while (i != connection.getContentLength()){
                    pw.print((char)connection.getInputStream().read());
                    ++i;
                }
                pw.close();
     
            	
            	            } catch (IOException e1) {
            	System.out.println("connection error");
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128)));



    }

}
