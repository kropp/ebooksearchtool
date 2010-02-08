package org.ebooksearchtool.analyzer.model;

import java.util.List;

/**
 * @author Алексей
 */

public class Author {
    private String myName;
    private List<String> myAliases;
    private String myID;
    private int myTrustIndex;
    private int myRelevanceIndex;

    public Author(){
        myName = "";
        myAliases = null;
        myID="";
    }
    
    public Author(String name){
        myName = name;
        myAliases = null;
        myID = "";
    }
    
     public Author(String name, List<String> aliases){
        myName = name;
        myAliases = aliases;
        myID = "";
    }

     public Author(int ID){
         myName = "";
         myAliases = null;
         myID = Integer.toString(ID);
     }

    /**
     * @return the myName
     */
    public String getName() {
        return myName;
    }

    /**
     * @param myName the myName to set
     */
    public void setName(String myName) {
        this.myName = myName;
    }

    /**
     * @return the myAliases
     */
    public List<String> getAliases() {
        return myAliases;
    }

    /**
     * @param myAliases the myAliases to set
     */
    public void setAliases(List<String> myAliases) {
        this.myAliases = myAliases;
    }

     /**
     * @return the myID
     */
    public String getID() {
        return myID;
    }

    /**
     * @param myID the myID to set
     */
    public void setID(String myID) {
        this.myID = myID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Author other = (Author) obj;
        if ((this.myName == null) ? (other.myName != null) : !this.myName.equals(other.myName)) {
            return false;
        }
        if (this.myAliases != other.myAliases && (this.myAliases == null || !this.myAliases.equals(other.myAliases))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (this.myName != null ? this.myName.hashCode() : 0)
                + (this.myAliases != null ? this.myAliases.hashCode() : 0);
    }

    /**
     * @return the myTrustIndex
     */
    public int getTrIndex() {
        return myTrustIndex;
    }

    /**
     * @param myTrustIndex the myTrustIndex to set
     */
    public void setTrIndex(int myTrustIndex) {
        this.myTrustIndex = myTrustIndex;
    }

    /**
     * @return the myRelevanceIndex
     */
    public int getRelIndex() {
        return myRelevanceIndex;
    }

    /**
     * @param myRelevanceIndex the myRelevanceIndex to set
     */
    public void setRelIndex(int myRelevanceIndex) {
        this.myRelevanceIndex = myRelevanceIndex;
    }
}
