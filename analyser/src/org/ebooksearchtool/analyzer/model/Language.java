package org.ebooksearchtool.analyzer.model;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Aleksey Podoplsky
 */

public class Language {
    private String myFullDescription;
    private List<String> myShortDescriptions;

    public Language(){
        myFullDescription = "";
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

    public String writeLanguage(){
        if(!this.getShortDescriptions().isEmpty()){
            StringBuilder str = new StringBuilder();
            str.append("<lang>");
            str.append(this.getShortDescriptions().get(0));
            str.append("</lang>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));

            return str.toString();
        }
        return "";
    }
}
