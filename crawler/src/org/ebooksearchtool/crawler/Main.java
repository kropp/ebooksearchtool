package org.ebooksearchtool.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    
    public static final File PROPERTIES_FILE = new File("crawler.properties");
    
    public static final String[][] DEFAULT_PROPERTIES = {
        {"user_agent", "ebooksearchtool"},
        {"connection_timeout", "5000"},
        {"read_timeout", "5000"},

        {"analyzer_enabled", "true"},
        {"analyzer_port", "9999"},

        {"proxy_enabled", "false"},
        {"proxy_type", "http"},
        {"proxy_host", "192.168.0.2"},
        {"proxy_port", "3128"},

        {"max_links_count", "0"},
        {"max_links_from_page", "0"},
        {"max_queue_size", "1000"},
        {"threads_count", "10"},
        {"thread_timeout_for_link", "20000"},
        {"thread_finish_time", "10000"},
        {"waiting_for_access_timeout", "5000"},
        
        {"large_amount_of_books", "10"},
        {"max_links_from_host", "30"},
        {"max_links_from_large_source", "60"},
        {"max_links_from_second_level_domain", "60"},
        {"host_stats_cleanup_period", "90000"},
        
        {"good_domains", "com net org info edu gov biz ru uk us"},
        {"good_sites", ""},
        {"bad_sites", "facebook wikipedia /wiki tumblr rutube endless amazon flickr blogspot wordpress livejournal"},

        {"debug", "true"},
        {"debug_file", "dump.txt"},
        {"found_books_file", "found.xml"},
        {"log_file", "log.txt"},
        {"log_to_screen", "true"},
        {"log_downloaded_robots_txt", "false"},
        {"log_crawled_pages", "true"},
        {"log_found_books", "true"},
        {"log_errors", "true"},
        {"log_misc", "true"}
    };
                                        
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        for (String[] property : DEFAULT_PROPERTIES) {
            properties.setProperty(property[0], property[1]);
        }
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException e) {
            System.err.println("cannot open file " + PROPERTIES_FILE + ", using default values instead");
        }
        
        if (args.length == 0) {
            System.out.println("usage:  java -jar bin/Crawler.jar www.example.com www.example.net www.example.org\n  if debug option is true,\n    input empty string and press Enter to ask crawler what is he doing\n    input any non-empty string and press Enter to exit");
            return;
        }
        
        final String[] starts = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            starts[i] = args[i].startsWith("http://") ? args[i] : "http://" + args[i];
        }
        
        boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));
        String debugFile = properties.getProperty("debug_file");
        String foundBooksFile = properties.getProperty("found_books_file");

        int maxRecordCount = Integer.parseInt(properties.getProperty("max_record_count"));
        maxRecordCount = maxRecordCount == 0 ? Integer.MAX_VALUE : maxRecordCount;
        
//        PrintWriter output = null;
//        if (!"".equals(foundBooksFile)) {
//            try {
//                output = new PrintWriter(foundBooksFile);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
        FoundBookLogConfigure(foundBooksFile, maxRecordCount);


        if (!Util.init()) {
            System.err.println("initialization failed");
            System.exit(0);
        }
        Crawler crawler = new Crawler(properties, starts);
        Thread crawlingThread = new Thread(crawler);
        crawlingThread.start();

        //try {
        //} catch (InterruptedException e) {
        //}
        
        if (debug) {
            String keyboardInput = null;
            Scanner keyboardScanner = new Scanner(System.in);
            String input = "";
            String whereTo = "".equals(debugFile) ? "screen" : debugFile;
            while (true) {
                try {
                    input = keyboardScanner.nextLine();
                } catch (Exception e) {
                    break;
                }
                if ("".equals(input)) {
                    if (crawler.dumpCurrentState(debugFile)) {
                        System.out.println("current state dumped successfully to " + whereTo);
                    } else {
                        System.out.println("there were problems while dumping current state to " + whereTo);
                    }
                } else break;
            }
            System.out.println("exit: " + input);
            crawlingThread.interrupt();
            try {
                crawlingThread.join();
            } catch (InterruptedException ie) { }
        } else try {
            crawlingThread.join();
           // Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ie) { }
//        if (output != null) {
//            output.close();
//        }
        System.exit(0);
    }


   /**
    * Simple file logging Handler.
    *
    * The FileSplitHandler writes to a set of files.
    * With a specified name and added counter "0", "1", "2", etc.
    * For a set of files, as each file reaches a given record number limit,
    * it is closed, and a new file opened.
    * The next file will be named like the previous file,
    * but the counter will be incremented.
    */
    static class FileSplitHandler extends java.util.logging.Handler {
        private int myRecordCount;
        private int myFileCount = 0;
        private java.util.logging.FileHandler myFileHandler = null;

        private final String myFileName;
        private final int myMaxRecordCount;

       /**
        * Initialize a FileSplitHandler to write to a set of files.
        *
        * When the given limit has been written to one file, 
        * another file will be opened.
        * @param fileName - the pattern for naming the output file
        * @param maxRecordCount - the maximum number of records to write 
        * to any one file
        */
        public FileSplitHandler(String fileName, int maxRecordCount) {
            this.myFileName = fileName;
            this.myMaxRecordCount = maxRecordCount;
            this.myRecordCount = maxRecordCount;
        }

        public void close() {
            if (myFileHandler != null) {
                try {
                    myFileHandler.close();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }

        public void flush() {
            if (myFileHandler != null)
                myFileHandler.flush();
        }

        public void publish(java.util.logging.LogRecord record) {
            if (myRecordCount == myMaxRecordCount) {
                myRecordCount = 0;
                nextFileHandler();
            }
            if (myFileHandler != null)
                myFileHandler.publish(record);
            myRecordCount++;
        }

       /**
        * Initialize the next FileHandler.
        *
        * Closes the current FileHandler, opens the next FileHandler.
        */
        private void nextFileHandler() {
            close();
            myFileHandler = null;
            try {
                myFileHandler = new java.util.logging.FileHandler(myFileName + "." + myFileCount, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myFileCount++;
        }

        protected void finalize() {
            close();
        }
    }


   /**
    * Configures "foundBookLog" logger.
    *
    * Configures "foundBookLog" logger with FileSplitHandler.
    * @param fileName - the pattern for naming the output file, if it is null or empty turns off logger
    * @param maxRecordCount - the maximum number of records to write to any one file
    */
    static private void FoundBookLogConfigure(String fileName, int maxRecordCount) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("foundBookLog");
        if (fileName != null && !"".equals(fileName)) {
            logger.setLevel(java.util.logging.Level.ALL);
            java.util.logging.XMLFormatter formatter = new java.util.logging.XMLFormatter();
            java.util.logging.Handler handler = new FileSplitHandler(fileName, maxRecordCount);

            handler.setFormatter(formatter);
            logger.addHandler(handler);
        } else {
            logger.setLevel(java.util.logging.Level.OFF);
        }
    }
    
}
