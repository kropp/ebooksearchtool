package org.ebooksearchtool.analyzer.model;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Алексей
 */

public class BookInfo {
    private String myID;
    private List<Author> myAuthors;
    private String myTitle;
    private List<File> myFiles;
    private String myLanguage;
    private List<String> myAnnotations;

    public static final int AUTHORS = 0;
    public static final int TITLE = 1;
    public static final int FILE = 2;
    public static final int LANGUAGE = 3;
    public static final int ANNOTATIONS = 4;
    public static final int ID = 100;

    public BookInfo(){
        myID = "";
        myAuthors = new ArrayList<Author>();
        //myAuthors.add(new Author());
        myTitle = "";
        myFiles = new ArrayList<File>();
        //myFiles.add(new File());
        myLanguage = "";
        myAnnotations = new ArrayList<String>();
        myAnnotations.add("");
    }

    public BookInfo(List<Author> authors, String title, List<File> files,
            String language, List<String> annotations){
        myAuthors = authors;
        myTitle = title;
        myFiles = files;
        myLanguage = language;
        myAnnotations = annotations;
    }

    public BookInfo(int id){
        myID = Integer.toBinaryString(id);
        myAuthors = new ArrayList<Author>();
        //myAuthors.add(new Author());
        myTitle = "";
        myFiles = new ArrayList<File>();
        //myFiles.add(new File());
        myLanguage = "";
        myAnnotations = new ArrayList<String>();
        myAnnotations.add("");
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

    public String getBookInfo(){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("<book>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        if(!myTitle.equals("")){
            str.append(writeTitle());
        }
        if(!myLanguage.equals("")){
            str.append(writeLanguage());
        }
        if(!myAuthors.isEmpty() && !myAuthors.get(0).equals("")){
            str.append(writeAuthors());
        }
        if(!myFiles.isEmpty()){
            str.append(writeFiles());
        }
        if(!myAnnotations.isEmpty() && !myAnnotations.get(0).equals("")){
            str.append(writeAnnotations());
        }
        str.append("</book>");

        return str.toString();
    }

    /**
     * Returns BookInfo from server answer message or null if messege don't
     * contain BookInfo
     * @param message to parse to BookInfo
     * @return BookInfo if parse succesfull or null otherwise.
     */
    public static BookInfo getBookInfoFromRequest(String message){
        BookInfo book = new BookInfo();
        StringBuilder sb = new StringBuilder(message);
        String temp = "";
        StringBuilder tempB = new StringBuilder();
        int endIndex = 0;
        if(sb.indexOf("<book") != -1 && sb.indexOf("</book>") != -1){
            int index = sb.indexOf("<book");
            if(index > 0){
                sb.delete(0, index);
            }
            index = sb.indexOf("<book id=\"");
            if(index != -1){
                temp = sb.substring(index + "<book id=\"".length());
                endIndex = temp.indexOf(">") - 1;
                temp = temp.substring(0, endIndex);
                book.setID(temp);
            }
            //Title
            index = sb.indexOf("<title>");
            if(index != -1){
                temp = sb.substring(index);
                endIndex = temp.indexOf("<");
                temp = sb.substring(index + "<title>".length() + 1, endIndex + index - 1);
                book.setTitle(temp);
            }
            //Language
            index = sb.indexOf("<language>");
            if(index != -1){
                temp = sb.substring(index);
                endIndex = temp.indexOf("<");
                temp = sb.substring(index + "<language>".length() + 1, endIndex + index - 1);
                book.setLanguage(temp);
            }
            //Authors
            index = sb.indexOf("<authors>");
            tempB = new StringBuilder(sb.substring(index + "<authors>".length()));
            //Второй скобки нет из-за возможного id
            while(tempB.indexOf("<author") != -1){
                Author author = new Author();
                index = sb.indexOf("<author");
                //TODO: Добавить работу с Aliases
                temp = tempB.substring(index);
                index = temp.indexOf("id=\"");
                temp = temp.substring(index + "id=\"".length());
                endIndex = temp.indexOf(">") - 1;
                temp = temp.substring(0, endIndex);
                author.setID(temp);

                index = temp.indexOf("<name>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);                  
                }
                author.setName(temp);
                book.addAuthor(author);
                tempB.delete(0, tempB.indexOf("</author>") + "</author>".length());
            }
            //Files
            index = sb.indexOf("<files>");
            tempB = new StringBuilder(sb.substring(index + "<files>".length()));
            //Второй скобки нет из-за возможного id
            while(tempB.indexOf("<file") != -1){
                File file = new File();
                index = sb.indexOf("<file");
                temp = tempB.substring(index);

                temp = tempB.substring(index);
                index = temp.indexOf("id=\"");
                temp = temp.substring(index + "id=\"".length());
                endIndex = temp.indexOf(">") - 1;
                temp = temp.substring(0, endIndex);
                file.setID(temp);

                index = temp.indexOf("<link>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setLink(temp);
                }
                index = temp.indexOf("<size>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setSize(temp);
                }
                index = temp.indexOf("<type>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setType(temp);
                }
                index = temp.indexOf("<time_found>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setTimeFound(temp);
                }
                index = temp.indexOf("<last_chek>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setLastChek(temp);
                }
                index = temp.indexOf("<more_info>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setMoreInfo(temp);
                }
                index = temp.indexOf("<img_link>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setImgLink(temp);
                }
                book.addFile(file);
                tempB.delete(0, tempB.indexOf("</file>") + "</file>".length());
            }
            //Annotations
            index = sb.indexOf("<annotation>");
            tempB = new StringBuilder(sb.substring(index));
            while(tempB.indexOf("<annotation>") != -1){
                index = sb.indexOf("<annotation>");
                if(index != -1){
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    book.addAnnotation(temp);
                    tempB.delete(0, tempB.indexOf("</annotation>") + "</annotation>".length());
                }
            }

            return book;
        }else{
            return null;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Get methods for requests">
    public String getBookInfoForRequest(){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("<book>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(writeTitle());
        str.append(writeLanguage());
        str.append(writeAuthors());
        str.append(writeFilesForRequest());
        str.append(writeAnnotations());
        str.append("</book>");

        return str.toString();
    }

    public String getBookInfoForBookIDRequest(String id){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append("<book id=\"" + id + "\">");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("</book>");

        return str.toString();
    }

    //Пока что выдает всю информацию по КНИГЕ (куча книг и файлов, в которые в
    //которые входит данный)
    public String getBookInfoForFileIDRequest(String id){
        StringBuilder str = new StringBuilder(getBookInfo());
        int index = str.indexOf("File");
        str.insert(index + 4, " id=\"" + id + "\"");
        return str.toString();
    }

    public String getBookInfoForAuthorIDRequest(String id){
        StringBuilder str = new StringBuilder(getBookInfo());
        int index = str.indexOf("Author");
        str.insert(index + 4, " id=\"" + id + "\"");
        return str.toString();
    }

    public String getBookInfoForBookIDReplace(String id){
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("<book id=\"" + id + ">");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(writeTitle());
        str.append(writeLanguage());
        str.append(writeAuthors());
        str.append(writeFilesForRequest());
        str.append(writeAnnotations());
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
            //Не убирать, просто пока не нужны
//            List<String> aliases = author.getAliases();
//            if(aliases != null){
//            int alLength = aliases.size();
//                for (int j = 0; j < alLength; j++) {
//                    str.append("<alias>");
//                    str.append(aliases.get(j));
//                    str.append("</alias>");
//                    str.append(AnalyzerProperties.getPropertie("systemSeparator"));
//                }
//            }
            str.append("</author>");
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        }
        str.append("</authors>");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        
        return str.toString();
    }

    private String writeFilesForRequest(){
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
            if(!file.getSize().equals("")){
                str.append("<size>");
                str.append(file.getSize());
                str.append("</size>");
            }
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            if(!file.getType().equals("")){
                str.append("<type>");
                str.append(file.getType());
                str.append("</type>");
            }
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            if(!file.getTimeFound().equals("")){
                str.append("<time_found>");
                str.append(file.getTimeFound());
                str.append("</time_found>");
            }
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            if(!file.getLastChek().equals("")){
                str.append("<last_check>");
                str.append(file.getLastChek());
                str.append("</last_check>");
            }
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            if(!file.getMoreInfo().equals("")){
                str.append("<more_info>");
                str.append(file.getMoreInfo());
                str.append("</more_info>");
            }
            str.append(AnalyzerProperties.getPropertie("systemSeparator"));
            if(!file.getImgLink().equals("")){
                str.append("<img_link>");
                str.append(file.getImgLink());
                str.append("</img_link>");
            }
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
