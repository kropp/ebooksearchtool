package org.ebooksearchtool.analyzer.model;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.utils.AnalyzerConstants;

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
        myAuthors.add(new Author());
        myTitle = "Unknown title";
        myFiles = new ArrayList<File>();
        myFiles.add(new File());
        myLanguage = "Unknown language";
        myAnnotations = new ArrayList<String>();
        myAnnotations.add("Unknown annotation");
    }

    public BookInfo(List<Author> authors, String title, List<File> files,
            String language, List<String> annotations){
        myAuthors = authors;
        myTitle = title;
        myFiles = files;
        myLanguage = language;
        myAnnotations = annotations;
    }

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
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        str.append("<book>");
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        str.append(writeTitle());
        str.append(writeLanguage());      
        str.append(writeAuthors());
        str.append(writeFiles());
        str.append(writeAnnotations());
        str.append("</book>");

        return str.toString();
    }

    private String writeTitle(){
        StringBuilder str = new StringBuilder();
        str.append("<title>");
        str.append(getTitle());
        str.append("</title>");
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);

        return str.toString();
    }

    private String writeLanguage(){
        StringBuilder str = new StringBuilder();
        str.append("<lang>");
        str.append(getLanguage());
        str.append("</lang>");
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);

        return str.toString();
    }

    private String writeAuthors(){
        StringBuilder str = new StringBuilder();
        List<Author> authors = getAuthors();
        int length = authors.size();

        str.append("<authors>");
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        for (int i = 0; i < length; i++) {
            Author author = authors.get(i);
            str.append("<author>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            str.append("<name>");
            str.append(author.getName());
            str.append("</name>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            List<String> aliases = author.getAliases();
            if(aliases != null){
            int alLength = aliases.size();
                for (int j = 0; j < alLength; j++) {
                    str.append("<alias>");
                    str.append(aliases.get(j));
                    str.append("</alias>");
                    str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
                }
            }
            str.append("<author>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        }
        str.append("</authors>");
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        
        return str.toString();
    }

    private String writeFiles(){
        StringBuilder str = new StringBuilder();
        List<File> files = getFiles();
        int length = files.size();

        str.append("<files>");
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        for (int i = 0; i < length; i++) {
            File file = files.get(i);
            str.append("<file>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            str.append("<link>");
            str.append(file.getLink());
            str.append("</link>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            str.append("<size>");
            str.append(file.getSize());
            str.append("</size>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            str.append("<type>");
            str.append(file.getType());
            str.append("</type>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            str.append("<more_info>");
            str.append(file.getMoreInfo());
            str.append("</more_info>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            str.append("<img_link>");
            str.append(file.getImgLink());
            str.append("</img_link>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
            str.append("</file>");
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        }
        str.append("</files>");
        str.append(AnalyzerConstants.SYSTEM_SEPARATOR);

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
            str.append(AnalyzerConstants.SYSTEM_SEPARATOR);
        }

        return str.toString();
    }

    public void addAuthor(Author author){
        myAuthors.add(author);
    }

    public void addFile(File file){
        myFiles.add(file);
    }
}
