package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {

	public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128));
	
	public static void main(String[] args) {
		if (args.length > 0) {
			final String start = args[0];
			PrintWriter output = null;
			try {
				output = new PrintWriter("visited.txt");
			} catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
				System.exit(0);
			}
			final PrintWriter finalOutput = output;
			new Thread(new Runnable() {
				public void run() {
					new Crawler(finalOutput).go(new String[]{start});
				}
			}).start();
			String keyboardInput = null;
			Scanner keyboardScanner = new Scanner(System.in);
			while (true) {
				keyboardInput = keyboardScanner.nextLine();
				if (keyboardInput.length() > 0) break;
			}
			System.out.println("exit: " + keyboardInput);
			output.flush();
			output.close();
			System.exit(0);
		} else {
			System.out.println("usage: java -jar Crawler.jar http://www.example.com/");
		}
	}
	
}
