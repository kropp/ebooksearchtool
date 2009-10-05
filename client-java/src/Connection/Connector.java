package Connection;

import java.net.*;
import java.io.*;
import View.Viewer;

import exec.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 22.09.2009
 * Time: 20:38:21
 * To change this template use File | Settings | File Templates.
 */
public class Connector{
	
	Viewer viewer;
    URL Url;
    URLConnection connection;

    public Connector(String adress) throws IOException {
        Url = new URL(adress);
        viewer = new Viewer();
    }

    public void GetFileFromURL() {
        
        try {
            connection = Url.openConnection();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("answer_file.xml"), "utf-8"));

            int i = 0;
            while (i != connection.getContentLength()){
                pw.print((char)connection.getInputStream().read());
                ++i;
            }
            pw.close();
        } catch (IOException e) {
            try {
                String IP = "192.168.0.2";
                int port = 3128;
                viewer.showProxyDialog(IP, port);
                connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(IP, port)));
                
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("answer_file.xml"), "utf-8"));

                int i = 0;
                while (i != connection.getContentLength()){
                    pw.print((char)connection.getInputStream().read());
                    ++i;
                }
                pw.close();
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //connection = Url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128)));



    }

}
