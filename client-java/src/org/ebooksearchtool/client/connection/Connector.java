package org.ebooksearchtool.client.connection;

import org.ebooksearchtool.client.model.Settings;

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
    Settings mySettings;

    public Connector(String adress, Settings set) throws IOException {
        Url = new URL(adress);
        mySettings = set;
    }

    public void getFileFromURL(String fileName) {
        
        try {
        	if(!mySettings.isProxyEnabled()){
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
        	}else{
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(mySettings.getIP(), mySettings.getPort())));
                PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
                
                int i = 0;
                while (i != connection.getContentLength() )
                {
                    pw1.print((char)connection.getInputStream().read());
                    ++i;
                    System.out.println(i);
                }
                pw1.close();
        	}
        } catch (IOException e) {
            
            	System.out.println("connection error");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            
        }



    }
    
public void getBookFromURL(String fileName) {
        
        try {
        	if(!mySettings.isProxyEnabled()){
            connection = Url.openConnection();
            File outFile = new File(fileName);
            FileOutputStream outStream = new FileOutputStream(outFile);
            InputStream IS = connection.getInputStream();

            int i = 0;
            while (i != -1)
            {
              	i = IS.read();
               	outStream.write(i);

            }
            outStream.close();
        	}else{
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(mySettings.getIP(), mySettings.getPort())));
                
                File outFile = new File(fileName);
                FileOutputStream outStream = new FileOutputStream(outFile);
                InputStream IS = connection.getInputStream();
                int i = 0;
                while (i != -1)
               {
                	i = IS.read();
                	outStream.write(i);
                    //pw1.print((char)connection.getInputStream().read());
                	
                }
                outStream.close();
        	}
            
        } catch (IOException e1) {
            	System.out.println("connection error");
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            
        }
        //connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128)));



    }

}
