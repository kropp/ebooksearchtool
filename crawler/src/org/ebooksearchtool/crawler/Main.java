package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;

public class Main {
    
    public static final File PROPERTIES_FILE = new File("crawler.properties");
    public static final File FOUND_BOOKS_FILE = new File("found.xml");
    public static final File DUMP_FILE = new File("dump.txt");
    
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        
        // default values
        properties.setProperty("connection_timeout", "3000");
        properties.setProperty("user_agent", "ebooksearchtool");
        
        properties.setProperty("analyzer_enabled", "true");
        properties.setProperty("analyzer_port", "9999");
        
        properties.setProperty("proxy_enabled", "false");
        properties.setProperty("proxy_type", "http");
        properties.setProperty("proxy_host", "192.168.0.2");
        properties.setProperty("proxy_port", "3128");
        
        properties.setProperty("max_links_count", "0");
        properties.setProperty("max_links_from_page", "0");
        properties.setProperty("threads_count", "10");

        properties.setProperty("log_file", "");
        properties.setProperty("log_to_screen", "true");
        
        
        
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
