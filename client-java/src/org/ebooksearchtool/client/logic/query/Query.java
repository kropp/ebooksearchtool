package org.ebooksearchtool.client.logic.query;

import org.ebooksearchtool.client.model.settings.Settings;

public class Query {
	
	Settings mySettings;
	
	public Query(Settings sets){
		
		mySettings = sets;
		
	}
	
	public String getQueryAdress(String server, String[] words) {

        String queryWord[] = new String[words.length];
        for(int i = 0; i < queryWord.length; ++i){
            queryWord[i] = words[i];
                queryWord[i] = queryWord[i].replaceAll(" ", "+");
        }

        if (server.equals("http://feedbooks.com")) {
            String link = mySettings.getSupportedServers().get(server).getSearchTerms();
            if (!"".equals(queryWord[0])) {
                link += queryWord[0];
            }
            if (!"".equals(queryWord[1])) {
                if ("".equals(queryWord[0])) {
                    link += "title:" + queryWord[1];
                } else {
                    link += "+title:" + queryWord[1];
                }
            }
            if (!"".equals(queryWord[2])) {
                if ("".equals(queryWord[0]) && "".equals(queryWord[1])) {
                    link += "author:" + queryWord[2];
                } else {
                    link += "+author:" + queryWord[2];
                }
            }
            return link;
        } else if (server.equals("http://only.mawhrin.net/ebooks")) {
            String link = mySettings.getSupportedServers().get(server).getSearchTerms();
            StringBuffer sb = new StringBuffer(link);
            sb.delete(sb.indexOf("query="), sb.length());
            link = sb.toString();
            if (!"".equals(queryWord[0])) {
                link += "query=" + queryWord[0];
            }
            if (!"".equals(queryWord[1])) {
                if ("".equals(queryWord[0])) {
                    link += "title=" + queryWord[1];
                } else {
                    link += ";title=" + queryWord[1];
                }
            }
            if (!"".equals(queryWord[2])) {
                if ("".equals(queryWord[0]) && "".equals(queryWord[1])) {
                    link += "author=" + queryWord[2];
                } else {
                    link += ";author=" + queryWord[2];
                }
            }
            return link;
        } else {
            return mySettings.getSupportedServers().get(server).getSearchTerms() + queryWord[0];
        }

	}

 /*   public String addQueryAdress(String server, String queryWord, String queryOption, String adress){

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
   */
}
