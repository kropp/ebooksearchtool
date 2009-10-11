package org.ebooksearchtool.client.Logic.query;

import org.ebooksearchtool.client.view.Viewer;

import java.io.IOException;

public class Query {
	
	String userQuery;
	Viewer viewer;
	
	public Query(){
		
		viewer = new Viewer();
		userQuery = "Tolstoy";
		
	}
	
	public String getQueryAdress()  throws IOException{
		
		
		
		return "http://feedbooks.com/books/search.atom?query=" + viewer.showQueryDialog();
		
	}

}
