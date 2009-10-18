package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;

public class Main {
    
    public static final String PROPERTIES_FILENAME = "crawler.properties";
    
    public static void main(String[] args) {
        Properties properties = new Properties();
        
        // default values
        properties.setProperty("max_links_count", "50000000");
        properties.setProperty("connection_timeout", "3000");
        properties.setProperty("user_agent", "ebooksearchtool");
        
        properties.setProperty("analyzer_enabled", "true");
        properties.setProperty("analyzer_port", "9999");
        
        properties.setProperty("proxy_enabled", "false");
        properties.setProperty("proxy_type", "http");
        properties.setProperty("proxy_host", "192.168.0.2");
        properties.setProperty("proxy_port", "3128");
        
        
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
            new Thread(new Runnable() {
                public void run() {
                    crawler.crawl(new String[]{start});
                }
            }).start();
            String keyboardInput = null;
            Scanner keyboardScanner = new Scanner(System.in);
            String input = "";
            while (true) {
                input = keyboardScanner.nextLine();
                if (input.length() == 0) {
                    System.out.println(crawler.getAction());
                } else break;
            }
            System.out.println("exit: " + input);
            crawler.stop();
            while (crawler.isRunning());
            output.close();
        } else {
            System.out.println("usage:\n  java -jar Crawler.jar www.example.com\n  input empty string and press Enter to ask Crawler what is he doing\n  input any non-empty string and press Enter to exit");
        }
    }
    
}
