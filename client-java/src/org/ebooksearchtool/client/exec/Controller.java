package org.ebooksearchtool.client.exec;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.logic.parsing.*;
import org.ebooksearchtool.client.logic.query.Query;
import org.ebooksearchtool.client.model.QueryAnswer;
import org.ebooksearchtool.client.model.books.Data;
import org.ebooksearchtool.client.model.settings.Server;
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

        //SAXParserTest.test();

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
            getSettingsFromFile();
        } catch (FileNotFoundException exeption){
            mySettings.setProxyEnabled(true);
            mySettings.setIP("192.168.0.2");
            mySettings.setPort(3128);
            mySettings.getSupportedServers().put("http://feedbooks.com", new Server("http://feedbooks.com", "http://feedbooks.com/books/search.atom?query=", true));
            mySettings.getSupportedServers().put("http://smashwords.com", new Server("http://smashwords.com", "http://smashwords.com/atom/search/books/any?query=", true));
            mySettings.getSupportedServers().put("http://manybooks.net", new Server("http://manybooks.net", "http://manybooks.net/stanza/search.php?q=", true));
            mySettings.getSupportedServers().put("http://bookserver.archive.org", new Server("http://bookserver.archive.org", "http://bookserver.archive.org/aggregator/opensearch?q=", true));
            writeSettings();
        }

    }

    public boolean getQueryAnswer(final String word) throws IOException, SAXException, ParserConfigurationException {

        class Downloader implements Runnable {

            String myServer;
            String myFileName;

            public Downloader(String server, String name) {
                myServer = server;
                myFileName = "server" + name + ".xml";
            }

            public void run() {

                if (mySettings.getSupportedServers().get(myServer).isEnabled()) {

                    String adress = new String();
                    synchronized (mySettings) {
                        Query query = new Query(mySettings);

                        try {
                            adress = query.getQueryAdress(myServer, word, "General");                   //TODO переделать!

                            Connector connect = new Connector(adress, mySettings);
                            connect.getFileFromURL(myFileName);
                        } catch (IOException e1) {

                            e1.printStackTrace();
                        }
                    }

                    synchronized (myData) {
                        try {
                            Parser parser = new Parser();
                            SAXHandler handler = new SAXHandler(myData);
                            parser.parse(myFileName, handler);
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (SAXException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                }

            }
        }

        Thread[] threads = new Thread[mySettings.getSupportedServers().size()];

        for (int i = 0; i < mySettings.getSupportedServers().size(); ++i) {

            threads[i] = new Thread(new Downloader(mySettings.getSupportedServers().keySet().toArray(new String[mySettings.getSupportedServers().size()])[i], Integer.toString(i)));
            threads[i].start();

        }

        for (int i = 0; i < mySettings.getSupportedServers().size(); ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return false;
    }

    
    public void getNextData() throws IOException, SAXException, ParserConfigurationException{
    	Connector connect = new Connector(myData.getNextPage(), mySettings);
        connect.getFileFromURL("answer_file.xml");
        Parser parser = new Parser();
        SAXHandler handler = new SAXHandler(myData);
        parser.parse("answer_file.xml", handler);
    }

    public Settings getSettings(){
        return mySettings;
    }
    
    public Settings getSettingsFromFile() throws SAXException, ParserConfigurationException, IOException {
        Parser parser = new Parser();
        SAXSetHandler handler = new SAXSetHandler(mySettings);
        parser.parse("settings.xml", handler);
        return mySettings;
    }

    public void writeSettings() throws FileNotFoundException, UnsupportedEncodingException {
        XMLBuilder builder = new XMLBuilder();
        builder.makeSettingsXML(mySettings);
    }

    public QueryAnswer getAnswer(){
        return myData;
    }
    
    public void saveModel(){
        if(myRequestCount > 9){
            for (int i = 0; i < 9; ++i) {
                BufferedReader in;
                String text = new String();
                try {
                    in = new BufferedReader(new InputStreamReader(new FileInputStream(Integer.toString(i + 1) + ".xml")));
                    while (in.ready()) {
                        text += in.readLine();
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(Integer.toString(i) + ".xml"), "utf-8"));
                    pw.print(text);
                    pw.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (FileNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            return;
        }
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
