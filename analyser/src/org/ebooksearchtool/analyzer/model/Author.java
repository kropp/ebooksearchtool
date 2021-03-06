package org.ebooksearchtool.analyzer.model;

import java.util.List;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Алексей
 */

public class Author {
    private String myName;
    private List<String> myAliases;
    private String myID;
    private int myCreditIndex;
    private int myRelevanceIndex;

    public Author(){
        myName = "";
        myAliases = null;
        myID="";
        myCreditIndex = 0;
        myRelevanceIndex = 0;
    }
    
    public Author(String name){
        myName = name;
        myAliases = null;
        myID = "";
        myCreditIndex = 0;
        myRelevanceIndex = 0;
    }
    
     public Author(String name, List<String> aliases){
        myName = name;
        myAliases = aliases;
        myID = "";
        myCreditIndex = 0;
        myRelevanceIndex = 0;
    }

     public Author(int ID){
        myName = "";
        myAliases = null;
        myID = Integer.toString(ID);
        myCreditIndex = 0;
        myRelevanceIndex = 0;
     }

    /**
     * @return the myName
     */
    public String getName() {
        return this.myName;
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
     * @return the myCreditIndex
     */
    public int getTrIndex() {
        return myCreditIndex;
    }

    /**
     * @param myCreditIndex the myCreditIndex to set
     */
    public void setTrIndex(int myCreditIndex) {
        this.myCreditIndex = myCreditIndex;
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

    public static String writeAuthors(List<Author> authors){
        StringBuilder str = new StringBuilder();
        int length = authors.size();

        str.append("<authors>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        for (int i = 0; i < length; i++) {
            Author author = authors.get(i);
            str.append("<author>");
            str.append(author.getName());
            //Не убирать, просто пока не нужны
//            List<String> aliases = author.getAliases();
//            if(aliases != null){
//            int alLength = aliases.size();
//                for (int j = 0; j < alLength; j++) {
//                    str.append("<alias>");
//                    str.append(aliases.get(j));
//                    str.append("</alias>");
//                    str.append(AnalyzerProperties.getPropertie("system_separator"));
//                }
//            }
            str.append("</author>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }
        str.append("</authors>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));

        return str.toString();
    }

    public static String writeAuthorsForSearch(List<Author> authors, String searchType){
        StringBuilder str = new StringBuilder();
        int length = authors.size();

        for (int i = 0; i < length; i++) {
            Author author = authors.get(i);
            str.append("<author>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<query>");
            str.append(author.getName());
            str.append("</query>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("</author>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }
        
        return str.toString();
    }

    public String writeAuthor(){
        StringBuilder str = new StringBuilder();
        str.append("<author>");
        str.append(this.getName());
        str.append("</author>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        return str.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="OLD Authors Request">
//    public static String writeAuthorsForRequest(List<Author> authors, String searchType){
//        StringBuilder str = new StringBuilder();
//        int length = authors.size();
//
//        str.append("<authors>");
//        str.append(AnalyzerProperties.getPropertie("system_separator"));
//        for (int i = 0; i < length; i++) {
//            Author author = authors.get(i);
//            str.append("<author type=\"");
//            str.append(searchType);
//            str.append("\">");
//            str.append(author.getName());
//            //Не убирать, просто пока не нужны
////            List<String> aliases = author.getAliases();
////            if(aliases != null){
////            int alLength = aliases.size();
////                for (int j = 0; j < alLength; j++) {
////                    str.append("<alias>");
////                    str.append(aliases.get(j));
////                    str.append("</alias>");
////                    str.append(AnalyzerProperties.getPropertie("system_separator"));
////                }
////            }
//            str.append("</author>");
//            str.append(AnalyzerProperties.getPropertie("system_separator"));
//        }
//        str.append("</authors>");
//        str.append(AnalyzerProperties.getPropertie("system_separator"));
//
//        return str.toString();
//    }
    //</editor-fold>
}
