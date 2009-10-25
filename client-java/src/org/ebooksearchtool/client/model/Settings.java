package org.ebooksearchtool.client.model;

import org.ebooksearchtool.client.logic.parsing.Parser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 25.10.2009
 * Time: 17:35:28
 * To change this template use File | Settings | File Templates.
 */
public class Settings {

    private String myIP;
    private int myPort;

    public Settings() throws SAXException, ParserConfigurationException {

        Parser parser = new Parser();

    }


    public String getIP() {
        return myIP;
    }

    public void setIP(String myIP) {
        this.myIP = myIP;
    }

    public int getPort() {
        return myPort;
    }

    public void setPort(int myPort) {
        this.myPort = myPort;
    }
}
