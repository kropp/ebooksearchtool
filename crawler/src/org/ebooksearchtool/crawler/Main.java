package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		if (args.length > 0) {
			final String start = args[0].startsWith("http://") ? args[0] : "http://" + args[0];
			PrintWriter output = null;
			try {
				output = new PrintWriter("books.txt");
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
				System.exit(0);
			}
			final Crawler crawler = new Crawler(output);
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
			output.close();
			System.exit(0);
		} else {
			System.out.println("usage:\n  java -jar Crawler.jar www.example.com\n  input empty string and press Enter to ask Crawler what is he doing\n  input any non-empty string and press Enter to exit");
		}
	}
	
}
