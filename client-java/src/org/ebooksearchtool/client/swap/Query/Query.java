package org.ebooksearchtool.client.Logic.query;

import org.ebooksearchtool.client.view.Viewer;

import java.io.IOException;

public class Query {
	
	String userQuery;
	
	public Query(){
		
		userQuery = "Tolstoy";
		
	}
	
	public String getQueryAdress(String queryWord)  throws IOException{
		
		return "http://feedbooks.com/books/search.atom?query=" + queryWord;
		
	}

}
