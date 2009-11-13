package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;

public class Main {
    
    public static final File PROPERTIES_FILE = new File("crawler.properties");
    public static final File FOUND_BOOKS_FILE = new File("found.xml");
    public static final File DUMP_FILE = new File("dump.txt");
    
    public static final String[][] DEFAULT_PROPERTIES = {
        {"connection_timeout", "5000"},
        {"user_agent", "ebooksearchtool"},

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

        {"log_file", ""},
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
            properties.load(new FileReader(PROPERTIES_FILE));
        } catch (IOException e) {
            System.err.println("cannot open file " + PROPERTIES_FILE + ", using default values instead");
        }
        
        if (args.length > 0) {
            final String[] starts = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                starts[i] = args[i].startsWith("http://") ? args[i] : "http://" + args[i];
            }
            PrintWriter output = null;
            try {
                output = new PrintWriter(FOUND_BOOKS_FILE);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
                System.exit(0);
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
                    if (crawler.dumpCurrentState(DUMP_FILE)) {
                        System.out.println("current state dumped successfully to " + DUMP_FILE);
                    } else {
                        System.out.println("there were problems while dumping current state to " + DUMP_FILE);
                    }
                } else break;
            }
            System.out.println("exit: " + input);
            crawlingThread.interrupt();
            try {
                crawlingThread.join();
            } catch (InterruptedException ie) { }
            output.close();
            System.exit(0);
        } else {
            System.out.println("usage:\n  java -jar Crawler.jar www.example.com www.example.net www.example.org\n  input empty string and press Enter to ask Crawler what is he doing\n  input any non-empty string and press Enter to exit");
        }
    }
    
}
