package org.ebooksearchtool.client.exec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.logic.parsing.Parser;
import org.ebooksearchtool.client.logic.parsing.SAXClearHandler;
import org.ebooksearchtool.client.logic.parsing.SAXHandler;
import org.ebooksearchtool.client.logic.parsing.SAXSetHandler;
import org.ebooksearchtool.client.logic.query.Query;
import org.ebooksearchtool.client.model.QueryAnswer;
import org.ebooksearchtool.client.model.books.Book;
import org.ebooksearchtool.client.model.books.Data;
import org.ebooksearchtool.client.model.settings.Server;
import org.ebooksearchtool.client.model.settings.Settings;
import org.ebooksearchtool.client.utils.StopParsingException;
import org.ebooksearchtool.client.utils.XMLBuilder;
import org.xml.sax.SAXException;

public class Controller implements Completive {

    Data myData;
    Data myLibrary;
    Settings mySettings;
    int myRequestCount;
    boolean myIsModelSaved;
    boolean myIsComplete;
    ExecutorService myThreads = Executors.newCachedThreadPool();

    public Controller() throws SAXException, ParserConfigurationException, IOException {

        //SAXParserTest.test();
        myData = new Data();
        myLibrary = new Data();
        mySettings = new Settings();

        File images = new File("images");
        images.mkdir();
        File books = new File("books");
        books.mkdir();

        myRequestCount = 0;
        for (; ;) {
            File answer = new File(Integer.toString(myRequestCount) + ".xml");
            if (answer.exists()) {
                ++myRequestCount;
            } else {
                answer.delete();
                break;
            }
        }

        File lib = new File("library.xml");
        if (lib.exists()) {
            Parser parser = new Parser();
            QueryAnswer answer = new QueryAnswer(myLibrary);
            SAXHandler handler = new SAXHandler(answer);
            parser.parse("library.xml", handler);
        }

        try {
            getSettingsFromFile();
        } catch (FileNotFoundException exeption) {
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

    public boolean getQueryAnswer(final String[] words) {

        myThreads = Executors.newCachedThreadPool();

        class Downloader implements Callable<Boolean> {

            String myServer;
            String myFileName;
            QueryAnswer myAnswer;
            InputStream myStream;
            Completive m_compliter;
            boolean myIsFinished = false;

            public Downloader(String server, String name, Completive completer) {
                myServer = server;
                myFileName = "server" + name + ".xml";
                myAnswer = new QueryAnswer(myData);
                m_compliter = completer;
            }

            public Boolean call() {
                if (mySettings.getSupportedServers().get(myServer).isEnabled()) {
                    String adress = new String();
                    Query query = new Query(mySettings);

                    try {
                        adress = query.getQueryAdress(myServer, words);
                        System.out.println("IS");
                        System.out.println("new Connector  " + adress);
                        Connector connect = new Connector(adress, mySettings);
                        System.out.println("get stream " + adress);


                        // AbstractInterruptibleChannel aic = new AbstractInterruptibleChannel();
                        myStream = connect.getFileFromURL(myFileName);

                        if (myStream == null) {
                            System.out.println("return null");
                            return false;
                        }

                        Parser parser = new Parser();
                        SAXHandler handler = new SAXHandler(myAnswer, m_compliter);
                        System.out.println("parse  " + adress);
                        parser.parse(myStream, handler);
                        System.out.println("parse is over  " + adress);
                    } catch (IOException e) {
                        System.out.println("exception in controller");
                        e.printStackTrace();
                        return false;
                    } catch (StopParsingException stop) {
                        System.out.println("catch");
                        return false;
                    } catch (SAXException e) {
                        e.printStackTrace();
                        return false;
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                        return false;
                    } catch (Exception e) {
                        System.out.println("simple exception in  " + myServer);
                        e.printStackTrace();
                        return false;
                    }


                    if (!"".equals(myAnswer.getNextPage())) {
                        getNextPage(myAnswer.getNextPage());
                    }

                }
                System.out.println(myServer + myIsFinished);
                myIsFinished = true;
                System.out.println(myServer + myIsFinished);
                return true;
            }

            public void getNextPage(String nextAddres) {

                System.out.println("next of " + myServer);

                if (nextAddres.indexOf("http") != 0) {
                    nextAddres = myServer + "/" + nextAddres;
                }

                try {
                    Connector connect = new Connector(nextAddres, mySettings);
                    myStream = connect.getFileFromURL(myFileName);

                    if (myStream == null)
                        return;

                    Parser parser = new Parser();
                    SAXHandler handler = new SAXHandler(myAnswer);
                    parser.parse(myStream, handler);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (StopParsingException stop){
                    System.out.println("catch");
                    return;
                } catch (SAXException e) {
                    e.printStackTrace();
                    return;
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    System.out.println("simple exception in  " + myServer);
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
            for (String s : servers) {
                mySettings.getSupportedServers().get(s).setEnabled(false);
            }
            mySettings.getSupportedServers().get("http://feedbooks.com").setEnabled(true);
            mySettings.getSupportedServers().get("http://only.mawhrin.net/ebooks").setEnabled(true);
        }

        myIsModelSaved = false;
        myIsComplete = false;

        Downloader[] mainTasks = new Downloader[mySettings.getSupportedServers().size()];
        for (int i = 0; i < mySettings.getSupportedServers().size(); ++i) {
            mainTasks[i] = new Downloader(mySettings.getSupportedServers().keySet().toArray(new String[mySettings.getSupportedServers().size()])[i], Integer.toString(i), this);
            System.out.println("task");
            addTask(mainTasks[i]);
        }

        System.out.println("try");

       /* try {
            while (!myThreads.awaitTermination(60, TimeUnit.SECONDS)) ;
        }
        catch (InterruptedException ie) {
            System.err.println("Unexpected pool shutdown");
            myThreads.shutdownNow();
        }*/
        for(int i = 0; i < mainTasks.length; ++i){
            while(!mainTasks[i].isFinished() && !myIsComplete){}
            System.out.println("finished " + i);
        }
        //while(!myIsComplete || myThreads.isShutdown()){}

        System.out.println("catched");

        if (myData.getBooks().size() != 0) {
            if (myIsModelSaved) {
                reWriteModel();
            } else {
                saveModel();
            }
        }

        try {
            getSettingsFromFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("return");

        return false;
    }

    public void stopProcesses() {

        myIsComplete = true;
        myThreads.shutdown();

        try {
            // Wait a while for existing tasks to terminate
            if (!myThreads.awaitTermination(5, TimeUnit.SECONDS)) {
                myThreads.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!myThreads.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            System.out.println("shut");
            myThreads.shutdownNow();
            // Preserve interrupt status
            //Thread.currentThread().interrupt();
        }

        myThreads = Executors.newCachedThreadPool(); 

    }

    public void addTask(Callable<?> task) {
        myThreads.submit(task);
    }

    public Settings getSettings() {
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

    public Data getData() {
        return myData;
    }

    public Data getLibrary() {
        return myLibrary;
    }

    public void addToLibrary(Book book) {
        myLibrary.addBook(book);
    }

    public void saveModel() {
        System.out.println("save");
        if (myRequestCount > 9) {
            try {
                Parser pars = new Parser();
                pars.parse("0.xml", new SAXClearHandler());
            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            for (int i = 0; i < 9; ++i) {
                File prev = new File(Integer.toString(i) + ".xml");
                prev.delete();
                File cur = new File(Integer.toString(i + 1) + ".xml");
                cur.renameTo(new File(Integer.toString(i) + ".xml"));
            }
            --myRequestCount;
        }
        XMLBuilder builder = new XMLBuilder();
        builder.makeXML(myData, Integer.toString(myRequestCount) + ".xml");
        ++myRequestCount;
        myIsModelSaved = true;
    }

    public void saveLibrary() {
        XMLBuilder builder = new XMLBuilder();
        builder.makeXML(myLibrary, "library.xml");
    }

    public void reWriteModel() {
        System.out.println("rewrite");
        XMLBuilder builder = new XMLBuilder();
        synchronized (myData) {
            --myRequestCount;
            builder.makeXML(myData, Integer.toString(myRequestCount) + ".xml");
            ++myRequestCount;
        }
    }

    public void loadModel(int number) {
        Parser parser;
        myIsModelSaved = true;

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

    public void clearModel() {
        myData = new Data();
    }


    public int getRequestCount() {
        return myRequestCount;
    }

    public void setRequestCount(int myRequestCount) {
        this.myRequestCount = myRequestCount;
    }

    public boolean isModelSaved() {
        return myIsModelSaved;
    }

    public Boolean isComplete() {
        return myIsComplete;
        //return myThreads.isShutdown();
    }

}
