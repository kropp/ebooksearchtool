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


import org.ebooksearchtool.client.view.BookPanel;
import org.ebooksearchtool.client.view.Window;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;


public class Controller {

    Data myData;
    Settings mySettings;
    int myRequestCount;

    public Controller() throws SAXException, ParserConfigurationException, IOException {

        //SAXParserTest.test();

        myData = new Data();
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

    public boolean getQueryAnswer(final String word, Window win) throws IOException, SAXException, ParserConfigurationException {

        class Downloader implements Runnable {

            String myServer;
            String myFileName;
            Window myWindow;
            QueryAnswer myAnswer;

            public Downloader(String server, String name, Window win) {
                myServer = server;
                myFileName = "server" + name + ".xml";
                myWindow = win;
                myAnswer = new QueryAnswer(myData);
            }

            public void run() {

                if (mySettings.getSupportedServers().get(myServer).isEnabled()) {

                    String adress = new String();
                    Query query = new Query(mySettings);

                    try {
                        adress = query.getQueryAdress(myServer, word, "General");                   //TODO переделать!

                        Connector connect = new Connector(adress, mySettings);
                        connect.getFileFromURL(myFileName);
                    } catch (IOException e1) {

                        e1.printStackTrace();
                    }

                    synchronized (myData) {
                        try {
                            Parser parser = new Parser();
                            SAXHandler handler = new SAXHandler(myAnswer, myWindow);
                            parser.parse(myFileName, handler);
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (SAXException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }

                    if(!"".equals(myAnswer.getNextPage())){
                        getNextPage(myAnswer.getNextPage());
                    }

                }

            }

            public void getNextPage(String nextAddres) {

                try {
                    Connector connect = new Connector(nextAddres, mySettings);
                    connect.getFileFromURL(myFileName);
                } catch (IOException e1) {

                    e1.printStackTrace();
                }

                try {
                    Parser parser = new Parser();
                    SAXHandler handler = new SAXHandler(myAnswer, myWindow);
                    synchronized (myData) {

                        parser.parse(myFileName, handler);
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SAXException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


                if (!"".equals(myAnswer.getNextPage())) {
                    getNextPage(myAnswer.getNextPage());
                }

            }

        }

        Thread[] threads = new Thread[mySettings.getSupportedServers().size()];

        for (int i = 0; i < mySettings.getSupportedServers().size(); ++i) {

            threads[i] = new Thread(new Downloader(mySettings.getSupportedServers().keySet().toArray(new String[mySettings.getSupportedServers().size()])[i], Integer.toString(i), win));
            threads[i].start();

        }

        for (int i = 0; i < mySettings.getSupportedServers().size(); ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if (myData.getBooks().size() != 0) {
            saveModel();
        }

        return false;
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

    public Data getData(){
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
            --myRequestCount;
        }
    	XMLBuilder builder = new XMLBuilder();
    	builder.makeXML(myData, Integer.toString(myRequestCount)+".xml");
    	++myRequestCount;
    }
    
    public void extendModel(){
    	--myRequestCount;
    	XMLBuilder builder = new XMLBuilder();
    	builder.makeXML(myData, Integer.toString(myRequestCount)+".xml");
    	++myRequestCount;
    }
    
    public void loadModel(int number, Window win){
    	clearModel();
    	Parser parser;
		        
        try {
        	parser = new Parser();
            QueryAnswer answer = new QueryAnswer(myData);
        	SAXHandler handler = new SAXHandler(answer, win);
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
    	myData = new Data();
    }


	public int getRequestCount() {
		return myRequestCount;
	}

	public void setRequestCount(int myRequestCount) {
		this.myRequestCount = myRequestCount;
	}
    
}
