package org.ebooksearchtool.analyzer.model;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Алексей
 */

public class BookInfo {
    private List<Author> myAuthors;
    private String myTitle;
    private List<File> myFiles;
    private String myLanguage;
    private List<String> myAnnotations;

    public BookInfo(){
        myAuthors = new ArrayList<Author>();
        //myAuthors.add(new Author());
        myTitle = "Unknown title";
        myFiles = new ArrayList<File>();
        //myFiles.add(new File());
        myLanguage = "Unknown language";
        myAnnotations = new ArrayList<String>();
        myAnnotations.add("Unknown annotation");
    }

    /**
     * This constructor returns empty BookInfo if flag equals true and special
     * fully empty message for requests if flag equals false.
     * @param flag indicate which type of message need to return.
     */

    public BookInfo(boolean flag){
        if(flag){
            new BookInfo();
        }else{
            myAuthors = new ArrayList<Author>();
            //myAuthors.add(new Author());
            myTitle = "";
            myFiles = new ArrayList<File>();
            //myFiles.add(new File());
            myLanguage = "";
            myAnnotations = new ArrayList<String>();
            myAnnotations.add("");
        }
    }

    public BookInfo(List<Author> authors, String title, List<File> files,
            String language, List<String> annotations){
        myAuthors = authors;
        myTitle = title;
        myFiles = files;
        myLanguage = language;
        myAnnotations = annotations;
    }

    // <editor-fold defaultstate="collapsed" desc="Get and Set methods">
    /**
     * @return the myAuthors
     */
    public List<Author> getAuthors() {
        return myAuthors;
    }

    /**
     * @param myAuthors the myAuthors to set
     */
    public void setAuthors(List<Author> myAuthors) {
        this.myAuthors = myAuthors;
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
     * @return the myFiles
     */
    public List<File> getFiles() {
        return myFiles;
    }

    /**
     * @param myFiles the myFiles to set
     */
    public void setFiles(List<File> myFiles) {
        this.myFiles = myFiles;
    }

    /**
     * @return the myLanguage
     */
    public String getLanguage() {
        return myLanguage;
    }

    /**
     * @param myLanguage the myLanguage to set
     */
    public void setLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    /**
     * @return the myAnnotations
     */
    public List<String> getAnnotations() {
        return myAnnotations;
    }

    /**
     * @param myAnnotations the myAnnotations to set
     */
    public void setAnnotations(List<String> myAnnotations) {
        this.myAnnotations = myAnnotations;
    }
    
    public String getBookInfo(){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("<book>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(writeTitle());
        str.append(writeLanguage());
        str.append(writeAuthors());
        str.append(writeFiles());
        str.append(writeAnnotations());
        str.append("</book>");

        return str.toString();
    }

    public String getBookInfoForRequest(){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("<book>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        if(!myTitle.equals("") && !myTitle.equals("Unknown title")){
            str.append(writeTitle());
        }
        if(!myLanguage.equals("") && !myLanguage.equals("Unknown language")){
            str.append(writeLanguage());
        }
        if(myAuthors.size() != 0 && !myAuthors.get(0).getName().equals("Unknown author")){
            str.append(writeAuthors());
        }
        str.append(writeFiles());
        if(myAuthors.size() != 0 && !myAuthors.get(0).getName().equals("Unknown annotation")){
            str.append(writeAnnotations());
        }
        str.append("</book>");

        return str.toString();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fields write methods">
    private String writeTitle(){
        StringBuilder str = new StringBuilder();
        str.append("<title>");
        str.append(getTitle());
        str.append("</title>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        return str.toString();
    }

    private String writeLanguage(){
        StringBuilder str = new StringBuilder();
        str.append("<lang>");
        str.append(getLanguage());
        str.append("</lang>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        return str.toString();
    }

    private String writeAuthors(){
        StringBuilder str = new StringBuilder();
        List<Author> authors = getAuthors();
        int length = authors.size();

        str.append("<authors>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        for (int i = 0; i < length; i++) {
            Author author = authors.get(i);
            str.append("<author>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<name>");
            str.append(author.getName());
            str.append("</name>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            List<String> aliases = author.getAliases();
            if(aliases != null){
            int alLength = aliases.size();
                for (int j = 0; j < alLength; j++) {
                    str.append("<alias>");
                    str.append(aliases.get(j));
                    str.append("</alias>");
                    str.append(AnalyzerProperties.getPropertie("systemSeparator"));
                }
            }
            str.append("</author>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        }
        str.append("</authors>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        
        return str.toString();
    }

    private String writeFiles(){
        StringBuilder str = new StringBuilder();
        List<File> files = getFiles();
        int length = files.size();

        str.append("<files>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        for (int i = 0; i < length; i++) {
            File file = files.get(i);
            str.append("<file>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<link>");
            str.append(file.getLink());
            str.append("</link>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<size>");
            str.append(file.getSize());
            str.append("</size>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<type>");
            str.append(file.getType());
            str.append("</type>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<time_found>");
            str.append(file.getTimeFound());
            str.append("</time_found>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<last_check>");
            str.append(file.getLastChek());
            str.append("</last_check>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<more_info>");
            str.append(file.getMoreInfo());
            str.append("</more_info>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("<img_link>");
            str.append(file.getImgLink());
            str.append("</img_link>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            str.append("</file>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        }
        str.append("</files>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        return str.toString();
    }

    private String writeAnnotations(){
        StringBuilder str = new StringBuilder();
        List<String> annotations = getAnnotations();
        int length = annotations.size();
        for (int i = 0; i < length; i++) {
            str.append("<annotation>");
            str.append(annotations.get(i));
            str.append("</annotation>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        }

        return str.toString();
    }
    //</editor-fold>

    public void addAuthor(Author author){
        myAuthors.add(author);
    }

    public void addAnnotation(String annotation){
        myAnnotations.add(annotation);
    }

    public void addFile(File file){
        myFiles.add(file);
    }
}
