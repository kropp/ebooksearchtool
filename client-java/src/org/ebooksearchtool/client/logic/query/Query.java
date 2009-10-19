package org.ebooksearchtool.client.logic.query;

import org.ebooksearchtool.client.view.Viewer;

import java.io.IOException;

public class Query {
	
	String userQuery;
	
	public Query(){
		
		userQuery = "Tolstoy";
		
	}
	
	public String getQueryAdress(String queryWord)  throws IOException{
		
		return "http://feedbooks.com/books/search.atom?query="/*"http://192.168.2.104:8001/books/search.atom/query="*/ + queryWord;
		
	}

}
