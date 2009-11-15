package org.ebooksearchtool.client.exec;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.logic.query.*;
import org.ebooksearchtool.client.logic.parsing.*;
import org.ebooksearchtool.client.model.Data;
import org.ebooksearchtool.client.model.Settings;
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

        File images = new File("images");
        images.mkdir();
        File books = new File("books");
        books.mkdir();
        

        try {
        	mySettings.setServer(getSettingsFromFile().getServer());
            mySettings.setProxyEnabled(getSettingsFromFile().isProxyEnabled());
            mySettings.setIP(getSettingsFromFile().getIP());
            mySettings.setPort(getSettingsFromFile().getPort());
        } catch (FileNotFoundException exeption){
            setSettings("http://feedbooks.com", true, "192.168.0.2", 3128);
        }

    }

	
    public boolean getQueryAnswer(String queryWord, String queryOption) throws IOException, SAXException, ParserConfigurationException {
        Query query = new Query();
        String adress = query.getQueryAdress(queryWord, queryOption);
        Connector connect = new Connector(mySettings.getServer()+adress, mySettings);
        if(connect.getFileFromURL("answer_file.xml")){
            Parser parser = new Parser();
            SAXHandler handler = new SAXHandler(myBooks);
            parser.parse("answer_file.xml", handler);
            return true;
        }else{
            return false;
        }
        
    }
    
    public void getNextData() throws IOException, SAXException, ParserConfigurationException{
    	Connector connect = new Connector(myBooks.getNextPage(), mySettings);
        connect.getFileFromURL("answer_file.xml");
        Parser parser = new Parser();
        SAXHandler handler = new SAXHandler(myBooks);
        parser.parse("answer_file.xml", handler);
    }

    public Settings getSettings() throws SAXException, ParserConfigurationException, IOException {
        return mySettings;
    }
    
    public Settings getSettingsFromFile() throws SAXException, ParserConfigurationException, IOException {
        Parser parser = new Parser();
        SAXSetHandler handler = new SAXSetHandler(mySettings);
        parser.parse("settings.xml", handler);
        return mySettings;
    }

    public void setSettings(String server, boolean proxy, String IP, int port) throws FileNotFoundException, UnsupportedEncodingException {
        mySettings.setServer(server);
    	mySettings.setIP(IP);
        mySettings.setPort(port);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("settings.xml"), "utf-8"));
        pw.print("<root>\n" + "<server>" + server + "</server>\n" + "<proxy enabled=\"" + proxy + "\"/>\n" + "<IP>" + IP + "</IP>\n" + "<port>" + port + "</port>\n" + "</root>");
        pw.close();
    }
    
    public boolean getBookFile(int bookIndex) throws IOException{
    	
    	Connector connect = new Connector(myBooks.getBooks().get(bookIndex).getPdfLink(), mySettings);
    	
    	return connect.getBookFromURL(myBooks.getBooks().get(bookIndex).getTitle() + ".pdf");
    	
    }

    public Data getData(){
        return myBooks;
    }
    
}
