package org.ebooksearchtool.client.logic.query;

import org.ebooksearchtool.client.model.settings.Settings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Query {
	
	Settings mySettings;
	
	public Query(Settings sets){
		
		mySettings = sets;
		
	}
	
	public String getQueryAdress(String server, String[] words) throws UnsupportedEncodingException {

        String queryWord[] = new String[words.length];
        for(int i = 0; i < queryWord.length; ++i){
            queryWord[i] = words[i];
                queryWord[i] = queryWord[i].replaceAll(" ", "+");
        }

        if (server.equals("http://feedbooks.com")) {
            String link = mySettings.getSupportedServers().get(server).getSearchTerms();
            if (!"".equals(queryWord[0])) {
                link += URLEncoder.encode(queryWord[0], "utf-8");
            }
            if (!"".equals(queryWord[1])) {
                if ("".equals(queryWord[0])) {
                    link += "title:" + URLEncoder.encode(queryWord[1], "utf-8");
                } else {
                    link += "+title:" + URLEncoder.encode(queryWord[1], "utf-8");
                }
            }
            if (!"".equals(queryWord[2])) {
                if ("".equals(queryWord[0]) && "".equals(queryWord[1])) {
                    link += "author:" + URLEncoder.encode(queryWord[2], "utf-8");
                } else {
                    link += "+author:" + URLEncoder.encode(queryWord[2], "utf-8");
                }
            }
            return link;
        } else if (server.equals("http://ebooksearch.webfactional.com")) {
            String link = mySettings.getSupportedServers().get(server).getSearchTerms();
            StringBuffer sb = new StringBuffer(link);
            sb.delete(sb.indexOf("query="), sb.length());
            link = sb.toString();
            if (!"".equals(queryWord[0])) {
                link += "query=" + URLEncoder.encode(queryWord[0], "utf-8");
            }
            if (!"".equals(queryWord[1])) {
                if ("".equals(queryWord[0])) {
                    link += "title=" + URLEncoder.encode(queryWord[1], "utf-8");
                } else {
                    link += ";title=" + URLEncoder.encode(queryWord[1], "utf-8");
                }
            }
            if (!"".equals(queryWord[2])) {
                if ("".equals(queryWord[0]) && "".equals(queryWord[1])) {
                    link += "author=" + URLEncoder.encode(queryWord[2], "utf-8");
                } else {
                    link += ";author=" + URLEncoder.encode(queryWord[2], "utf-8");
                }
            }
            return link;
        } else {
            return mySettings.getSupportedServers().get(server).getSearchTerms() + URLEncoder.encode(queryWord[0], "utf-8");
        }

	}

}
