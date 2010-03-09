package org.ebooksearchtool.client.model.settings;

/* Date: 07.03.2010
 * Time: 22:06:52
 */
public class Server {

    String myName;
    String mySearchTerm;
    boolean myIsEnabled;

    public Server(String name, String term, boolean en){
        myName = name;
        mySearchTerm = term;
        myIsEnabled = en;
    }

    public String getName(){
        return myName;
    }

    public String getSearchTerms(){
        return mySearchTerm;
    }

    public boolean isEnabled(){
        return myIsEnabled;
    }

    public void setName(String name){
        myName = name;
    }

    public void setSearchTerm(String term){
        mySearchTerm = term;
    }

    public void setEnabled(boolean en){
        myIsEnabled = en;
    }

}
