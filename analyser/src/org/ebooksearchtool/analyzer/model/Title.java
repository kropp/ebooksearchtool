package org.ebooksearchtool.analyzer.model;

import java.util.List;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Aleksey Podolskiy
 */

public class Title {
    private String myTitle;
    private String myID;
    private int mycreditIndex;
    private int myRelevanceIndex;


    public Title(){
        myTitle = "";
        myID = "";
        mycreditIndex = 0;
        myRelevanceIndex = 0;
    }

    public Title(String title){
        myTitle = title;
        myID = "";
        mycreditIndex = 0;
        myRelevanceIndex = 0;
    }

    public Title(String title, int credit, int relev){
        myTitle = title;
        myID = "";
        mycreditIndex = credit;
        myRelevanceIndex = relev;
    }

    /**
     * @return the mycreditIndex
     */
    public int getTrIndex() {
        return mycreditIndex;
    }

    /**
     * @param mycreditIndex the mycreditIndex to set
     */
    public void setTrIndex(int mycreditIndex) {
        this.mycreditIndex = mycreditIndex;
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
    public String getName() {
        return myTitle;
    }

    /**
     * @param myTitle the myTitle to set
     */
    public void setName(String myTitle) {
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

    public String writeTitle(){
        StringBuilder str = new StringBuilder();
        str.append("<title>");
        str.append(this.getName());
        str.append("</title>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));

        return str.toString();
    }

    public String writeTitleForRequest(List<String> IDs){
        StringBuilder str = new StringBuilder();
        str.append("<book_title>");
        if(!IDs.isEmpty()){
            str.append("<authors_id>");            
            for(String id : IDs){
                str.append("<author_id>");                
                str.append(id);                
                str.append("</author_id>");
            }
            str.append("</authors_id>");            
        }
        str.append("<query>");
        str.append(this.getName());
        str.append("<query>");
        str.append("</book_title>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));

        return str.toString();
    }

   //<editor-fold defaultstate="collapsed" desc="OLD Title Request">
//    public String writeTitleForRequest(String searchType){
//        StringBuilder str = new StringBuilder();
//        str.append("<title type=\"");
//        str.append(searchType);
//        str.append("\">");
//        str.append(this.getName());
//        str.append("</title>");
//        str.append(AnalyzerProperties.getPropertie("system_separator"));
//
//        return str.toString();
//    }
    //</editor-fold>
}
