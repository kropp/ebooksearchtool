package org.ebooksearchtool.client.logic.query;

import java.io.IOException;

import org.ebooksearchtool.client.model.settings.Settings;

public class Query {
	
	Settings mySettings;
	
	public Query(Settings sets){
		
		mySettings = sets;
		
	}
	
	public String getQueryAdress(String queryWord, String queryOption)  throws IOException{

		if(mySettings.getServer().equals("http://feedbooks.com")){
			if(queryOption.equals("General")){
				return "/books/search.atom?query=" + queryWord;
			}else if(queryOption.equals("Author")){
				return "/books/search.atom?query=author:" + queryWord;
			}else{
				return "/books/search.atom?query=title:" + queryWord;
			}
		}else if(mySettings.getServer().equals("http://smashwords.com")){
            return "/atom/search/books/any?query=" + queryWord;
        }else if(mySettings.getServer().equals("http://manybooks.net")){
            return "/stanza/search.php?q=" + queryWord;
        }else if(mySettings.getServer().equals("http://bookserver.archive.org")){
            return "/aggregator/opensearch?q=" + queryWord;
        }else{
			if(queryOption.equals("General")){
				return "/ebooks/search.atom?query=" + queryWord;
			}else if(queryOption.equals("Author")){
				return "/ebooks/search.atom?author=" + queryWord;
			}else{
				return "/ebooks/search.atom?title=" + queryWord;
			}
		}

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
