package org.ebooksearchtool.analyzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksey Podoplsky
 */

public class Language {
    private String myFullDescription;
    private List<String> myShortDescriptions;

    public Language(){
        myFullDescription = "Unknown language";
        myShortDescriptions = new ArrayList<String>();
    }

    public Language(String fullDesc) {
        myFullDescription = fullDesc;
        myShortDescriptions = new ArrayList<String>();
    }

    public Language(String fullDesc, List<String> shortDesc){
        myFullDescription = fullDesc;
        myShortDescriptions = shortDesc;
    }

    // <editor-fold defaultstate="collapsed" desc="Get and Set methods">
    /**
     * @return the myFullDescription
     */
    public String getFullDescription() {
        return myFullDescription;
    }

    /**
     * @param myFullDescription the myFullDescription to set
     */
    public void setFullDescription(String myFullDescription) {
        this.myFullDescription = myFullDescription;
    }

    /**
     * @return the myShortDescriptions
     */
    public List<String> getShortDescriptions() {
        return myShortDescriptions;
    }

    /**
     * @param myShortDescriptions the myShortDescriptions to set
     */
    public void setShortDescriptions(List<String> myShortDescriptions) {
        this.myShortDescriptions = myShortDescriptions;
    }

    public void addShortDescription(String desc) {
        myShortDescriptions.add(desc);
    }
    
    // </editor-fold>
}
