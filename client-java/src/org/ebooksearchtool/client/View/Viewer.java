package org.ebooksearchtool.client.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Viewer {
	
	public void showProxyDialog(String IP, int port) throws IOException{
		
		String inputIP;
		String inputPort;
		BufferedReader bReader = new BufferedReader (new InputStreamReader(System.in)); 
        System.out.println("input IP (192.168.0.2 for APTU)");
        inputIP = bReader.readLine();
        if (!inputIP.equals("")){
        	IP = inputIP;
        }
        System.out.println("input port (3128 for APTU)");
        inputPort = bReader.readLine();
        if(!inputPort.equals("")){
        	port = Integer.parseInt(inputPort);
        }
		
	}
	
	public String showQueryDialog() throws IOException{
		
		String inputQuery;
		BufferedReader bReader = new BufferedReader (new InputStreamReader(System.in)); 
        System.out.println("Enter query word");
        /*inputQuery*/return bReader.readLine();
      //  if (inputQuery != ""){
      //  	query = inputQuery;
      //  }
		
	}

}
