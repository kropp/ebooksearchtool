package org.ebooksearchtool.analyzer.model;

/**
 * @author Aleksey Podolskiy
 */

public class Title {
    private String myTitle;
    private String myID;
    private int myTrustIndex;
    private int myRelevanceIndex;


    public Title(){
        myTitle = "";
        myID = "";
        myTrustIndex = 0;
        myRelevanceIndex = 0;
    }

    public Title(String title){
        myTitle = title;
        myID = "";
        myTrustIndex = 0;
        myRelevanceIndex = 0;
    }

    public Title(String title, int trust, int relev){
        myTitle = title;
        myID = "";
        myTrustIndex = trust;
        myRelevanceIndex = relev;
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

    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @param myTitle the myTitle to set
     */
    public void setTitle(String myTitle) {
        this.myTitle = myTitle;
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
}
