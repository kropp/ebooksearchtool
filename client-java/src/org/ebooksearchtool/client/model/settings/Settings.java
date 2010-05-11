package org.ebooksearchtool.client.model.settings;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;

/*
 * Date: 25.10.2009
 * Time: 17:35:28
 */
public class Settings {

    private String myIP, myReader;
    private int myPort;
    private boolean myProxyIsEnabled;

    private HashMap<String,  Server> mySupportedServers = new HashMap<String,  Server>();

    public Settings() throws SAXException, ParserConfigurationException {}
    
    public String getIP() {
        return myIP;
    }

    public void setIP(String IP) {
        this.myIP = IP;
    }

    public int getPort() {
        return myPort;
    }

    public void setPort(int myPort) {
        this.myPort = myPort;
    }

    public boolean isProxyEnabled() {
        return myProxyIsEnabled;
    }

    public void setProxyEnabled(boolean enabled) {
        myProxyIsEnabled = enabled;
    }

    public HashMap<String, Server> getSupportedServers() {
        return mySupportedServers;
    }

    public String getReader() {
        return myReader;
    }

    public void setReader(String reader) {
        this.myReader = reader;
    }
}
