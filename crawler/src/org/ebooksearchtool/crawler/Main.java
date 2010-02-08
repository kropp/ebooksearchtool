package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;

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
        {"max_links_from_host", "40"},
        {"max_links_from_large_source", "60"},
        {"host_stats_cleanup_period", "60000"},

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
        
        PrintWriter output = null;
        if (!"".equals(foundBooksFile)) {
            try {
                output = new PrintWriter(foundBooksFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!Util.init()) {
            System.err.println("initialization failed");
            System.exit(0);
        }
        Crawler crawler = new Crawler(properties, starts, output);
        Thread crawlingThread = new Thread(crawler);
        crawlingThread.start();
        
        String keyboardInput = null;
        Scanner keyboardScanner = new Scanner(System.in);
        String input = "";
        while (true) {
            input = keyboardScanner.nextLine();
            if (input.length() == 0) {
                if (crawler.dumpCurrentState(debugFile)) {
                    System.out.println("current state dumped successfully to " + ("".equals(debugFile) ? "screen" : debugFile));
                } else {
                    System.out.println("there were problems while dumping current state to " + ("".equals(debugFile) ? "screen" : debugFile));
                }
            } else break;
        }
        System.out.println("exit: " + input);
        crawlingThread.interrupt();
        try {
            crawlingThread.join();
        } catch (InterruptedException ie) { }
        if (output != null) {
            output.close();
        }
        System.exit(0);
    }
    
}
