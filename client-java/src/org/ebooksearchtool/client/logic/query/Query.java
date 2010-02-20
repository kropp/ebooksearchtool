package org.ebooksearchtool.client.logic.query;

import java.io.IOException;

import org.ebooksearchtool.client.model.settings.Settings;

public class Query {
	
	Settings mySettings;
	
	public Query(Settings sets){
		
		mySettings = sets;
		
	}
	
	public String getQueryAdress(String queryWord, String queryOption)  throws IOException{

		return mySettings.getSupportedServers().get(mySettings.getServer()) + queryWord;

	}

    public String addQueryAdress(String queryWord, String queryOption, String adress){

    	if(mySettings.getServer().equals("http://feedbooks.com")){
    		if(queryOption.equals("General")){
            	return adress + "+" + queryWord;
        	}else if(queryOption.equals("Author")){
            	return adress + "+author:" + queryWord;
        	}else{
            	return adress + "+title:" + queryWord;
        	}
    	}else{
    		if(queryOption.equals("General")){
                return adress + ";query=" + queryWord;
            }else if(queryOption.equals("Author")){
                return adress + ";author=" + queryWord;
            }else{
                return adress + ";title=" + queryWord;
            }
    	}

    }

}
