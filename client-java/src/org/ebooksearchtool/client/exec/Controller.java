package org.ebooksearchtool.client.exec;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.logic.query.*;
import org.ebooksearchtool.client.logic.parsing.*;
import org.ebooksearchtool.client.model.Data;
import org.ebooksearchtool.client.model.Settings;
import org.ebooksearchtool.client.view.Viewer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


/**
 * Created by IntelliJ IDEA.
 * User: 
 * Date: 01.10.2009
 * Time: 21:29:17
 * To change this template use File | Settings | File Templates.
 */
public class Controller {

    Data myBooks;
    Settings mySettings;

    public Controller() throws SAXException, ParserConfigurationException, IOException {

        myBooks = new Data();
        mySettings = new Settings();

        try {
            mySettings.setIP(getSettings().getIP());
            mySettings.setPort(getSettings().getPort());
        } catch (FileNotFoundException exeption){
            setSettings("192.168.0.2", 3128);
        }

    }

	
    public void getQueryAnswer(String queryWord) throws IOException, SAXException, ParserConfigurationException {
        Query query = new Query();
        String adress = query.getQueryAdress(queryWord);
        Connector connect = new Connector(adress, mySettings.getIP(), mySettings.getPort());
        connect.getFileFromURL("answer_file.xml");
        Parser parser = new Parser();
        SAXHandler handler = new SAXHandler(myBooks);
        parser.parse("answer_file.xml", handler);

    }

    public Settings getSettings() throws SAXException, ParserConfigurationException, IOException {
        Parser parser = new Parser();
        SAXSetHandler handler = new SAXSetHandler(mySettings);
        parser.parse("settings.xml", handler);
        return mySettings;
    }

    public void setSettings(String IP, int port) throws FileNotFoundException, UnsupportedEncodingException {
        mySettings.setIP(IP);
        mySettings.setPort(port);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("settings.xml"), "utf-8"));
        pw.print("<root>\n" + "<IP>" + IP + "</IP>\n" + "<port>" + port + "</port>\n" + "</root>");
        pw.close();
    }
    
    public void getBookFile(int bookIndex) throws IOException{
    	
    	Connector connect = new Connector(myBooks.getInfo().get(bookIndex).getLink(), mySettings.getIP(), mySettings.getPort());
    	
    	connect.getFileFromURL("book.pdf");
    	
    }

    public Data getData(){
        return myBooks;
    }

}
