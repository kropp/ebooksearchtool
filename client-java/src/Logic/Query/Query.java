package Logic.Query;

import java.io.IOException;

import View.Viewer;

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