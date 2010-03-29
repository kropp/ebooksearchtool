package org.ebooksearchtool.client.exec;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.logic.parsing.*;
import org.ebooksearchtool.client.logic.query.Query;
import org.ebooksearchtool.client.model.QueryAnswer;
import org.ebooksearchtool.client.model.books.Data;
import org.ebooksearchtool.client.model.settings.Server;
import org.ebooksearchtool.client.model.settings.Settings;
import org.ebooksearchtool.client.utils.XMLBuilder;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Controller {

    Data myData;
    Settings mySettings;
    int myRequestCount;
    int myModelCounter;
    ExecutorService myThreads;

    public Controller() throws SAXException, ParserConfigurationException, IOException {

        //SAXParserTest.test();
        myThreads = Executors.newCachedThreadPool();
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

    public boolean getQueryAnswer(final String[] words) throws IOException, SAXException, ParserConfigurationException {

        class Downloader implements Runnable {

            String myServer;
            String myFileName;
            QueryAnswer myAnswer;

            boolean myIsFinished;

            public Downloader(String server, String name) {
                myServer = server;
                myFileName = "server" + name + ".xml";
                myAnswer = new QueryAnswer(myData);
                myIsFinished = false;
            }

            public void run() {

                if (mySettings.getSupportedServers().get(myServer).isEnabled()) {
                    String adress = new String();
                    Query query = new Query(mySettings);
                    adress = query.getQueryAdress(myServer, words);

                    try {
                        System.out.println("IS");
                        InputStream is;
                        System.out.println("new Connector  " + adress);
                        Connector connect = new Connector(adress, mySettings);
                        System.out.println("get stream " + adress);
                        is = connect.getFileFromURL(myFileName);

                        Parser parser = new Parser();
                        SAXHandler handler = new SAXHandler(myAnswer);
                        System.out.println("parse  " + adress);
                        parser.parse(is, handler);
                        System.out.println("parse is over  " + adress);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    } catch (SAXException e) {
                        e.printStackTrace();
                        return;
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                        return;
                    }


                    if (!"".equals(myAnswer.getNextPage())) {
                        getNextPage(myAnswer.getNextPage());
                    }

                }

                myIsFinished = true;

            }

            public void getNextPage(String nextAddres) {

                try {
                    Connector connect = new Connector(nextAddres, mySettings);
                    InputStream is = connect.getFileFromURL(myFileName);

                    Parser parser = new Parser();
                    SAXHandler handler = new SAXHandler(myAnswer);
                    parser.parse(is, handler);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (SAXException e) {
                    e.printStackTrace();
                    return;
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    return;
                }


                if (!"".equals(myAnswer.getNextPage())) {
                    getNextPage(myAnswer.getNextPage());
                }

            }

            public boolean isFinished(){
                return myIsFinished;
            }

        }

        if (!"".equals(words[1]) || !"".equals(words[2])) {
            String[] servers = mySettings.getSupportedServers().keySet().toArray(new String[mySettings.getSupportedServers().size()]);
            for(String s : servers){
                mySettings.getSupportedServers().get(s).setEnabled(false);
            }
            mySettings.getSupportedServers().get("http://feedbooks.com").setEnabled(true);
            mySettings.getSupportedServers().get("http://only.mawhrin.net/ebooks").setEnabled(true);
        }

        Downloader[] mainTasks = new Downloader[mySettings.getSupportedServers().size()];
        for (int i = 0; i < mySettings.getSupportedServers().size(); ++i) {
            mainTasks[i] = new Downloader(mySettings.getSupportedServers().keySet().toArray(new String[mySettings.getSupportedServers().size()])[i], Integer.toString(i));
            System.out.println("task");
            addTask(mainTasks[i]);
        }
                                                       
        for(int i = 0; i < mainTasks.length; ++i) {
            while(!mainTasks[i].isFinished()){}
        }
        if (myData.getBooks().size() != 0) {
            saveModel();
        }

        getSettingsFromFile();

        return false;
    }

    public void stopProcesses(){

        myThreads.shutdown();

        try {
            // Wait a while for existing tasks to terminate
            if (!myThreads.awaitTermination(1, TimeUnit.SECONDS)) {
                myThreads.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!myThreads.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            myThreads.shutdownNow();
            // Preserve interrupt status
            //Thread.currentThread().interrupt();
        }


        /*synchronized (myData) {
           for (Thread t : myT) {
               t.stop();
               System.out.println("stoped");
           }
           myTasks.removeAll(myTasks);
       }
       if (myData.getBooks().size() != 0) {
           saveModel();
       } */

    }

    public Thread addTask(Runnable task){
        Thread t = new Thread(task);
        myThreads.submit(task);
        //t.start();
        return t;
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
        System.out.println("save");
        if(myRequestCount > 9){
            for (int i = 0; i < 9; ++i) {
                File prev = new File(Integer.toString(i) + ".xml");
                prev.delete();
                File cur = new File(Integer.toString(i + 1) + ".xml");
                cur.renameTo(new File(Integer.toString(i) + ".xml"));
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
    
    public void loadModel(int number){
    	clearModel();
    	Parser parser;
		        
        try {
        	parser = new Parser();
            QueryAnswer answer = new QueryAnswer(myData);
        	SAXHandler handler = new SAXHandler(answer);
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
