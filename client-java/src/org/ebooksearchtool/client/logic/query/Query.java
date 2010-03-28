package org.ebooksearchtool.client.logic.query;

import org.ebooksearchtool.client.model.settings.Settings;

public class Query {
	
	Settings mySettings;
	
	public Query(Settings sets){
		
		mySettings = sets;
		
	}
	
	public String getQueryAdress(String server, String queryWord, String queryOption) {

        queryWord = queryWord.replaceAll(" ", "+");
        if (server.equals("http://feedbooks.com")) {
            if (queryOption.equals("General")) {
                return mySettings.getSupportedServers().get(server).getSearchTerms() + queryWord;
            } else if (queryOption.equals("Author")) {
                return mySettings.getSupportedServers().get(server).getSearchTerms() + "author:" + queryWord;
            } else {
                return mySettings.getSupportedServers().get(server).getSearchTerms() + "title:" + queryWord;
            }
        } else {
            return mySettings.getSupportedServers().get(server).getSearchTerms() + queryWord;
        }

	}

    public String addQueryAdress(String server, String queryWord, String queryOption, String adress){

    	if(server.equals("http://feedbooks.com")){
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
