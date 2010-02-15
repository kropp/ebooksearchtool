package org.ebooksearchtool.analyzer.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Алексей
 */

public class File {
    private String myID;
    private String myLink;
    private String mySize;
    private String myType;
    private String myTimeFound;
    private String myLastChek;
    private String myMoreInfo;
    private String myImgLink;

    public File(){
        myID = "";
        myLink = "";
        mySize = "";
        myType = "";
        myTimeFound = "";
        myLastChek = "";
        myMoreInfo = "";
        myImgLink = "";
    }

    public File(String link, String size, String type, String moreInfo,
            String imageLink){
        myID = "";
        myLink = link;
        mySize = size;
        myType = type;
        myTimeFound = "";
        myLastChek = "";
        myMoreInfo = moreInfo;
        myImgLink = imageLink;
    }

    public File(String link){
        myID = "";
        myLink = link;
        mySize = "";
        myType = "";
        myTimeFound = "";
        myLastChek = "";
        myMoreInfo = "";
        myImgLink = "";
    }

    // <editor-fold defaultstate="collapsed" desc="Get and Set methods">
    /**
     * @return the myLink
     */
    public String getLink() {
        return myLink;
    }

    /**
     * @param myLink the myLink to set
     */
    public void setLink(String myLink) {
        this.myLink = myLink;
    }

    /**
     * @return the mySize
     */
    public String getSize() {
        return mySize;
    }

    /**
     * @param mySize the mySize to set
     */
    public void setSize(String mySize) {
        this.mySize = mySize;
    }

    /**
     * @return the myType
     */
    public String getType() {
        return myType;
    }

    /**
     * @param myType the myType to set
     */
    public void setType(String myType) {
        this.myType = myType;
    }

    /**
     * @return the myMoreInfo
     */
    public String getMoreInfo() {
        return myMoreInfo;
    }

    /**
     * @param myMoreInfo the myMoreInfo to set
     */
    public void setMoreInfo(String myMoreInfo) {
        this.myMoreInfo = myMoreInfo;
    }

    /**
     * @return the myImgLink
     */
    public String getImgLink() {
        return myImgLink;
    }

    /**
     * @param myImgLink the myImgLink to set
     */
    public void setImgLink(String myImgLink) {
        this.myImgLink = myImgLink;
    }

    /**
     * @return the myTimeFound
     */
    public String getTimeFound() {
        return myTimeFound;
    }

    /**
     * @param myTimeFound the myTimeFound to set
     */
    public void setTimeFound(String myTimeFound) {
        this.myTimeFound = myTimeFound;
    }

    /**
     * @return the mylastChek
     */
    public String getLastChek() {
        return myLastChek;
    }

    /**
     * @param mylastChek the mylastChek to set
     */
    public void setLastChek(String mylastChek) {
        this.myLastChek = mylastChek;
    }

    public static File formFileFromMap(Map<String, String> map){
        File file = new File();
        for(Entry<String, String> en : map.entrySet()){
            if(en.getKey().equals("Link")){
                file.setLink(en.getValue());
            }
            if(en.getKey().equals("Size")){
                file.setSize(en.getValue());
            }
            if(en.getKey().equals("ImgLink")){
                file.setImgLink(en.getValue());
            }
            if(en.getKey().equals("Type")){
                file.setType(en.getValue());
            }
            if(en.getKey().equals("LastChek")){
                file.setLastChek(en.getValue());
            }
            if(en.getKey().equals("MoreInfo")){
                file.setMoreInfo(en.getValue());
            }
            if(en.getKey().equals("TimeFound")){
                file.setTimeFound(en.getValue());
            }
        }
        return file;
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
    // </editor-fold>

    public static String writeFilesForRequest(List<File> files){
        StringBuilder str = new StringBuilder();
        int length = files.size();

        str.append("<files>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        for (int i = 0; i < length; i++) {
            File file = files.get(i);
            str.append("<file>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<link>");
            str.append(file.getLink());
            str.append("</link>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<size>");
            str.append(file.getSize());
            str.append("</size>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<type>");
            str.append(file.getType());
            str.append("</type>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<time_found>");
            str.append(file.getTimeFound());
            str.append("</time_found>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<last_check>");
            str.append(file.getLastChek());
            str.append("</last_check>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<more_info>");
            str.append(file.getMoreInfo());
            str.append("</more_info>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<img_link>");
            str.append(file.getImgLink());
            str.append("</img_link>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("</file>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }
        str.append("</files>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));

        return str.toString();
    }

    public static String writeFiles(List<File> files){
        StringBuilder str = new StringBuilder();
        int length = files.size();

        str.append("<files>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        for (int i = 0; i < length; i++) {
            File file = files.get(i);
            str.append("<file>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("<link>");
            str.append(file.getLink());
            str.append("</link>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            if(!file.getSize().equals("")){
                str.append("<size>");
                str.append(file.getSize());
                str.append("</size>");
            }
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            if(!file.getType().equals("")){
                str.append("<type>");
                str.append(file.getType());
                str.append("</type>");
            }
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            if(!file.getTimeFound().equals("")){
                str.append("<time_found>");
                str.append(file.getTimeFound());
                str.append("</time_found>");
            }
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            if(!file.getLastChek().equals("")){
                str.append("<last_check>");
                str.append(file.getLastChek());
                str.append("</last_check>");
            }
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            if(!file.getMoreInfo().equals("")){
                str.append("<more_info>");
                str.append(file.getMoreInfo());
                str.append("</more_info>");
            }
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            if(!file.getImgLink().equals("")){
                str.append("<img_link>");
                str.append(file.getImgLink());
                str.append("</img_link>");
            }
            str.append(AnalyzerProperties.getPropertie("system_separator"));
            str.append("</file>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }
        str.append("</files>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));

        return str.toString();
    }
}
