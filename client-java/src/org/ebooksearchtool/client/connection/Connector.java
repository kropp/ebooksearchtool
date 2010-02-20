package org.ebooksearchtool.client.connection;

import org.ebooksearchtool.client.model.settings.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.BoundedRangeModel;

/* Date: 22.09.2009
 * Time: 20:38:21
 */
public class Connector{
	
    URL Url;
    URLConnection connection;
    Settings mySettings;

    public Connector(String adress, Settings set) throws IOException {
        Url = new URL(adress);
        mySettings = set;
    }

    public boolean getFileFromURL(String fileName) {
        
        try {
        	if(!mySettings.isProxyEnabled()){
                connection = Url.openConnection();
                PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
                int i = connection.getInputStream().read();
                while (i!=-1){
                	pw1.print((char)i);
                    i = connection.getInputStream().read();
            	                                       
                }
                pw1.close();
        	}else{
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(mySettings.getIP(), mySettings.getPort())));
                PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
                
                int i = connection.getInputStream().read();
                while (i != -1 ){
                	pw1.print((char)i);
                	i = connection.getInputStream().read();
                }
                pw1.close();
        	}
            return true;
        } catch (IOException e) {
            
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;

        }

    }
    
public boolean getBookFromURL(String fileName, BoundedRangeModel model) {
        
        try {
        	if(!mySettings.isProxyEnabled()){
        		connection = Url.openConnection();
        		File outFile = new File(fileName);
        		FileOutputStream outStream = new FileOutputStream(outFile);
        		InputStream IS = connection.getInputStream();

        		model.setValue(0);
        		int proc = connection.getContentLength() / 100;
        		int j = 0;
        		int i = 0;
        		while (i != -1){
        			i = IS.read();
        			outStream.write(i);
        			++j;
        			if(j == proc){
        				model.setValue(model.getValue() + 1);
        				j = 0;
        			}
        		}
        		outStream.close();
        	}else{
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(mySettings.getIP(), mySettings.getPort())));
                
                File outFile = new File(fileName);
                FileOutputStream outStream = new FileOutputStream(outFile);
                InputStream IS = connection.getInputStream();
                int i = 0;
                model.setValue(0);
        		int proc = connection.getContentLength() / 100;
        		int j = 0;
                while (i != -1){
                	i = IS.read();
                	outStream.write(i);
                    //pw1.print((char)connection.getInputStream().read());     
                	++j;
        			if(j == proc){
        				model.setValue(model.getValue() + 1);
        				j = 0;
        			}
                }
                outStream.close();
        	}
            return true;
            
        } catch (IOException e1) {

            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;

        }
        //connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128)));



    }

}
