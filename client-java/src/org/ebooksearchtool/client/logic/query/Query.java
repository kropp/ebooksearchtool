package org.ebooksearchtool.client.logic.query;

import java.io.IOException;

public class Query {
	
	String userQuery;
	
	public Query(){
		
		userQuery = "Tolstoy";
		
	}
	
	public String getQueryAdress(String queryWord, String queryOption)  throws IOException{

        if(queryOption.equals("General")){
            return "/books/search.atom?query="/*"http://192.168.2.104:8001/books/search.atom/query="*/ + queryWord;
        }else if(queryOption.equals("Author")){
            return "/books/search.atom?query=author:" + queryWord;
        }else{
            return "/books/search.atom?query=title:" + queryWord;
        }

	}

    public String addQueryAdress(String queryWord, String queryOption, String adress){

        if(queryOption.equals("General")){
            return adress + "+" + queryWord;
        }else if(queryOption.equals("Author")){
            return adress + "+author:" + queryWord;
        }else{
            return adress + "+title:" + queryWord;
        }

    }

}
