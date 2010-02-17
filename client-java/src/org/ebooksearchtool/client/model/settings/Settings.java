package org.ebooksearchtool.client.model.settings;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/*
 * Date: 25.10.2009
 * Time: 17:35:28
 */
public class Settings {

	private String myServer;
    private String myIP;
    private int myPort;
    private boolean myProxyIsEnabled;

    public Settings() throws SAXException, ParserConfigurationException {}

    public String getServer() {
        return myServer;
    }

    public void setServer(String server) {
        this.myServer = server;
    }
    
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
}
