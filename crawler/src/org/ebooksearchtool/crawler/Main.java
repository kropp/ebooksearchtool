package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;

public class Main {
    
    public static final String PROPERTIES_FILENAME = "crawler.properties";
    
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
        
        
        
        try {
            properties.load(new FileReader(PROPERTIES_FILENAME));
        } catch (IOException e) {
            System.err.println("cannot open file " + PROPERTIES_FILENAME + ", using default values instead");
        }
        
        if (args.length > 0) {
            final String start = args[0].startsWith("http://") ? args[0] : "http://" + args[0];
            PrintWriter output = null;
            try {
                output = new PrintWriter("found.xml");
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
                System.exit(0);
            }
            final Crawler crawler = new Crawler(properties, output);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    crawler.crawl(new String[]{start});
                }
            });
            t.start();
            String keyboardInput = null;
            Scanner keyboardScanner = new Scanner(System.in);
            String input = "";
            while (true) {
                input = keyboardScanner.nextLine();
                if (input.length() == 0) {
                    System.out.println("disabled");
                } else break;
            }
            System.out.println("exit: " + input);
//            try { t.join(); } catch (InterruptedException e) { }
            output.close();
            System.exit(0);
        } else {
            System.out.println("usage:\n  java -jar Crawler.jar www.example.com\n  input empty string and press Enter to ask Crawler what is he doing\n  input any non-empty string and press Enter to exit");
        }
    }
    
}
