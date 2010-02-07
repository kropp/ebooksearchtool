package org.ebooksearchtool.client.exec;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.logic.parsing.*;
import org.ebooksearchtool.client.model.QueryAnswer;
import org.ebooksearchtool.client.model.books.Data;
import org.ebooksearchtool.client.model.settings.Settings;
import org.ebooksearchtool.client.tests.SAXParserTest;
import org.ebooksearchtool.client.utils.XMLBuilder;


import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


public class Controller {

    QueryAnswer myData;
    Settings mySettings;
    int myRequestCount;

    public Controller() throws SAXException, ParserConfigurationException, IOException {

        SAXParserTest.test();

        myData = new QueryAnswer();
        mySettings = new Settings();

        File images = new File("images");
        images.mkdir();
        File books = new File("books");
        books.mkdir();
        
        myRequestCount = 0;
        for(;;){
        	File answer = new File(Integer.toString(myRequestCount)+".xml");
        	if(answer.exists()){
        		++myRequestCount;
        	}else{
        		answer.delete();
        		break;
        	}
        }

        try {
        	mySettings.setServer(getSettingsFromFile().getServer());
            mySettings.setProxyEnabled(getSettingsFromFile().isProxyEnabled());
            mySettings.setIP(getSettingsFromFile().getIP());
            mySettings.setPort(getSettingsFromFile().getPort());
        } catch (FileNotFoundException exeption){
        	
            setSettings("http://feedbooks.com", true, "192.168.0.2", 3128);
        }

    }

	
    public boolean getQueryAnswer(String adress) throws IOException, SAXException, ParserConfigurationException {
        
        Connector connect = new Connector(mySettings.getServer() + adress, mySettings);
        if(connect.getFileFromURL("answer_file.xml")){
            Parser parser = new Parser();
            SAXHandler handler = new SAXHandler(myData);
            parser.parse("answer_file.xml", handler);
            return true;
        }else{
            return false;
        }
        
    }
    
    public void getNextData() throws IOException, SAXException, ParserConfigurationException{
    	Connector connect = new Connector(myData.getNextPage(), mySettings);
        connect.getFileFromURL("answer_file.xml");
        Parser parser = new Parser();
        SAXHandler handler = new SAXHandler(myData);
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
        mySettings.setProxyEnabled(proxy);
    	mySettings.setIP(IP);
        mySettings.setPort(port);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("settings.xml"), "utf-8"));
        pw.print("<root>\n" + "<server>" + server + "</server>\n" + "<proxy enabled=\"" + proxy + "\"/>\n" + "<IP>" + IP + "</IP>\n" + "<port>" + port + "</port>\n" + "</root>");
        pw.close();
    }
    
 /*   public boolean getBookFile(int bookIndex) throws IOException{
    	
    	Connector connect = new Connector(myBooks.getBooks().get(bookIndex).getPdfLink(), mySettings);
    	
    	return connect.getBookFromURL(myBooks.getBooks().get(bookIndex).getTitle() + ".pdf");
    	
    }*/

    public QueryAnswer getAnswer(){
        return myData;
    }
    
    public void saveModel(){
    	XMLBuilder builder = new XMLBuilder();
    	builder.makeXML(myData.getData(), Integer.toString(myRequestCount)+".xml");
    	++myRequestCount;
    }
    
    public void extendModel(){
    	--myRequestCount;
    	XMLBuilder builder = new XMLBuilder();
    	builder.makeXML(myData.getData(), Integer.toString(myRequestCount)+".xml");
    	++myRequestCount;
    }
    
    public void loadModel(int number){
    	clearModel();
    	Parser parser;
		        
        try {
        	parser = new Parser();
        	SAXHandler handler = new SAXHandler(myData);
			parser.parse(number + ".xml", handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void clearModel(){   	
    	myData = new QueryAnswer();
    }


	public int getRequestCount() {
		return myRequestCount;
	}

	public void setRequestCount(int myRequestCount) {
		this.myRequestCount = myRequestCount;
	}
    
}
