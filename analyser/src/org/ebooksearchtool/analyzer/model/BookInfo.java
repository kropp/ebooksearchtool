package org.ebooksearchtool.analyzer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;

/**
 * @author Алексей
 */
public class BookInfo {

    private String myID;
    private List<Author> myAuthors;
    private Title myTitle;
    private List<File> myFiles;
    private Language myLanguage;
    private List<String> myAnnotations;
    public static final int AUTHORS = 0;
    public static final int TITLE = 1;
    public static final int FILE = 2;
    public static final int LANGUAGE = 3;
    public static final int ANNOTATIONS = 4;
    public static final int ID = 100;
    //Request types
    public static final int AUTHOR_UPDATE = 101;
    public static final int BOOK_CREATION = 102;
    public static final int BOOK_UPDATE = 103;
    public static final int BOOK_FILES_RESET = 104;
    public static final int BOOK_AUTHORS_RESET = 105;

    public BookInfo() {
        myID = "";
        myAuthors = new ArrayList<Author>();
        //myAuthors.add(new Author());
        myTitle = new Title();
        myFiles = new ArrayList<File>();
        //myFiles.add(new File());
        myLanguage = new Language();
        myAnnotations = new ArrayList<String>();
        //myAnnotations.add("");
    }

    public BookInfo(List<Author> authors, String title, List<File> files,
            String language, List<String> annotations) {
        myAuthors = authors;
        myTitle = new Title(title);
        myFiles = files;
        myLanguage = new Language(language);
        myAnnotations = annotations;
    }

    public BookInfo(List<Author> authors, Title title, List<File> files,
            String language, List<String> annotations) {
        myAuthors = authors;
        myTitle = title;
        myFiles = files;
        myLanguage = new Language(language);
        myAnnotations = annotations;
    }

    public BookInfo(int id) {
        myID = Integer.toBinaryString(id);
        myAuthors = new ArrayList<Author>();
        //myAuthors.add(new Author());
        myTitle = new Title();
        myFiles = new ArrayList<File>();
        //myFiles.add(new File());
        myLanguage = new Language();
        myAnnotations = new ArrayList<String>();
        //myAnnotations.add("");
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
    public Title getTitle() {
        return myTitle;
    }

    /**
     * @param myTitle the myTitle to set
     */
    public void setTitle(Title myTitle) {
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
    public Language getLanguage() {
        return myLanguage;
    }

    /**
     * @param myLanguage the myLanguage to set
     */
    public void setLanguage(String myLanguage) {
        this.myLanguage = new Language(myLanguage);
    }

    public void setLanguage(Language myLanguage) {
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
    
    public String getBookInfo() {
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        str.append("<book>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        if (!myTitle.getName().equals("")) {
            str.append(this.getTitle().writeTitle());
        }
        if (!myLanguage.getFullDescription().equals("")) {
            str.append(this.getLanguage().writeLanguage());
        }
        if (!myAuthors.isEmpty() && !myAuthors.get(0).getName().equals("")) {
            str.append(Author.writeAuthors(this.getAuthors()));
        }
        if (!myFiles.isEmpty()) {
            str.append(File.writeFiles(this.getFiles()));
        }
        if (!myAnnotations.isEmpty() && !myAnnotations.get(0).equals("")) {
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
    //TODO:Uncomment rel and credit reads when server will mantains it
    private static BookInfo getBookInfoFromRequest(String message) {
        BookInfo book = new BookInfo();
        StringBuilder sb = new StringBuilder(message);
        String temp = "";
        StringBuilder tempB = new StringBuilder();
        int endIndex = 0;
        if (sb.indexOf("<book") != -1 && sb.indexOf("</book>") != -1) {
            int index = sb.indexOf("<book");
            if (index > 0) {
                sb.delete(0, index);
            }
            index = sb.indexOf("<book id=\"");
            if (index != -1) {
                temp = sb.substring(index + "<book id=\"".length());
                endIndex = temp.indexOf(">") - 1;
                temp = temp.substring(0, endIndex);
                book.setID(temp);
            }
            //Title
            index = sb.indexOf("<title");
            Title tit = new Title();
            if (index != -1) {
                temp = sb.substring(index);
                temp = temp.substring(temp.indexOf("rel=\""));
                endIndex = temp.indexOf("\"");
                tit.setRelIndex(Integer.valueOf(temp.substring("rel=\"".length(), endIndex)));
                temp = temp.substring(temp.indexOf("credit=\""));
                endIndex = temp.indexOf("\"");
                tit.setRelIndex(Integer.valueOf(temp.substring("credit=\"".length(), endIndex)));
                temp = temp.substring(temp.indexOf(">"));
                endIndex = temp.indexOf("<");
                tit.setName(temp.substring(1, endIndex));
                book.setTitle(tit);
            }
            //Language
            index = sb.indexOf("<language>");
            if (index != -1) {
                temp = sb.substring(index);
                endIndex = temp.indexOf("<");
                temp = sb.substring(index + "<language>".length() + 1, endIndex + index - 1);
                book.setLanguage(temp);
            }
            //Authors
            index = sb.indexOf("<authors>");
            tempB = new StringBuilder(sb.substring(index + "<authors>".length()));
            //Второй скобки нет из-за возможного id
            while (tempB.indexOf("<author") != -1) {
                Author author = new Author();
                index = sb.indexOf("<author");
                //TODO: Добавить работу с Aliases
                temp = tempB.substring(index);

                index = temp.indexOf("id=\"");
                if (index != -1) {
                    temp = temp.substring(index + "id=\"".length());
                    endIndex = temp.indexOf("\"");
                    author.setID(temp.substring("id=\"".length(), endIndex));
                } else {
                    author.setID("-1");
                }
                index = temp.indexOf("rel=\"");
                if (index != -1) {
                    temp = temp.substring(index + "rel=\"".length());
                    endIndex = temp.indexOf("\"");
                    author.setRelIndex(Integer.valueOf(temp.substring("rel=\"".length(), endIndex)));
                } else {
                    author.setRelIndex(0);
                }
                index = temp.indexOf("credit=\"");
                if (index != -1) {
                    temp = temp.substring(index + "credit=\"".length());
                    endIndex = temp.indexOf("\"");
                    author.setTrIndex(Integer.valueOf(temp.substring("credit=\"".length(), endIndex)));
                } else {
                    author.setTrIndex(0);
                }


                index = temp.indexOf("<name>");
                if (index != -1) {
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
            while (tempB.indexOf("<file") != -1) {
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
                if (index != -1) {
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setLink(temp);
                }
                index = temp.indexOf("<size>");
                if (index != -1) {
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setSize(temp);
                }
                index = temp.indexOf("<type>");
                if (index != -1) {
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setType(temp);
                }
                index = temp.indexOf("<time_found>");
                if (index != -1) {
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setTimeFound(temp);
                }
                index = temp.indexOf("<last_chek>");
                if (index != -1) {
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setLastChek(temp);
                }
                index = temp.indexOf("<more_info>");
                if (index != -1) {
                    temp = temp.substring(index);
                    index = temp.indexOf(">") + 1;
                    temp = temp.substring(index);
                    endIndex = temp.indexOf("<") - 1;
                    temp = temp.substring(index, endIndex + index);
                    file.setMoreInfo(temp);
                }
                index = temp.indexOf("<img_link>");
                if (index != -1) {
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
            while (tempB.indexOf("<annotation>") != -1) {
                index = sb.indexOf("<annotation>");
                if (index != -1) {
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
        } else {
            return null;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Get methods for requests">
    //Сейчас спрашиваю только автора и назавание
    public String getBookInfoForSearch(int reqType) {
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        str.append("<search>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        str.append("<result_length>");
        str.append(AnalyzerProperties.getPropertie("number_of_request_results"));
        str.append("</result_length>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        if (reqType == AUTHORS) {
            str.append(Author.writeAuthorsForSearch(this.getAuthors(), AnalyzeUtils.CONTAINS_CASE_INSENSITIVE));
            //str.append(writeLanguage());
            //str.append(writeFilesForRequest());
            //str.append(writeAnnotations());
        } else {
            if (reqType == TITLE) {
                str.append(this.getTitle().writeTitleForRequest(this.formAuthorsIDList()));
            }
        }
        str.append("</search>");

        return str.toString();
    }

    /**
     * Return BookInfo as String object for request that difine by updateTupe 
     * type. It may be:
     * AUTHOR_UPDATE,
     * BOOK_CREATION,
     * BOOK_UPDATE,
     * BOOK_FILES_RESET,
     * BOOK_AUTHORS_RESET.
     * @param updateType type of update action
     * @return BookInfo for difined action in String format
     */
    public String getBookInfoForUpdate(int updateType) {
        Map<String, Integer> uicontains = new HashMap<String, Integer>();
        int ui = 1;
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        str.append("<request>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        str.append("<define>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        int length = this.getAuthors().size();
        for (int i = 0; i < length; i++) {
            Author a = this.getAuthors().get(i);
            if (a.getID().equals("")) {
                uicontains.put(a.getName(), ui);
                str.append("<author ui=\"" + ui++ + "\">");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<full_name>");
                str.append(a.getName());
                str.append("</full_name>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("</author>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
            }
        }
        uicontains.put("book", ui);
        str.append("<book ui=\"" + ui++ + "\">");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        this.getTitle().writeTitle();
        this.getLanguage().writeLanguage();
        str.append("</book>");
        uicontains.put("file", ui);
        str.append("<file>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        File file = this.getFiles().get(0);//TODO:Remember about zero link - new link
        str.append("<link>");
        str.append(file.getLink());
        str.append("</link>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        if (!file.getType().equals("")) {
            str.append("<type>");
            str.append(file.getType());
            str.append("</type>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }
        if (!file.getSize().equals("")) {
            str.append("<size>");
            str.append(file.getSize());
            str.append("</size>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }
        str.append("</file>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        str.append("</define>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));

        str.append("<update>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        switch (updateType) {
            case AUTHOR_UPDATE: {
                length = this.getAuthors().size();
                for (int i = 0; i < length; i++) {
                    str.append("<author id=\"" + this.getAuthors().get(i).getID() + "\">");
                    str.append(AnalyzerProperties.getPropertie("system_separator"));
                    str.append("<full_name>");
                    str.append(this.getAuthors().get(i).getName());
                    str.append("</full_name>");
                    str.append(AnalyzerProperties.getPropertie("system_separator"));
                    str.append("</author>");
                }
                break;
            }
            case BOOK_CREATION: {
                str.append("<book ui=\"" + uicontains.get("book") + "\">");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<authors>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                for (int i = 0; i < length; i++) {
                    Author a = this.getAuthors().get(i);
                    if (a.getID().equals("")) {
                        str.append("<author ui=\"" + uicontains.get(a.getName()) + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    } else {
                        str.append("<author id=\"" + a.getID() + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    }
                }
                str.append("</authors>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<files>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<file ui=\"" + uicontains.get("file") + "\" />");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("</files>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("</book>");
                break;
            }
            case BOOK_UPDATE: {
                str.append("<book id=\"" + this.getID() + "\">");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<authors>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                for (int i = 0; i < length; i++) {
                    Author a = this.getAuthors().get(i);
                    if (a.getID().equals("")) {
                        str.append("<author ui=\"" + uicontains.get(a.getName()) + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    } else {
                        str.append("<author id=\"" + a.getID() + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    }
                }
                str.append("</authors>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<files>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<file ui=\"" + uicontains.get("file") + "\" />");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("</files>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("</book>");
                break;
            }
            case (BOOK_AUTHORS_RESET):{
                str.append("<book id=\"" + this.getID() + "\" reset=\"author\">");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<authors>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                for (int i = 0; i < length; i++) {
                    Author a = this.getAuthors().get(i);
                    if (a.getID().equals("")) {
                        str.append("<author ui=\"" + uicontains.get(a.getName()) + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    } else {
                        str.append("<author id=\"" + a.getID() + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    }
                }
                str.append("</authors>");
                str.append("</book>");
                break;
            }
            case (BOOK_FILES_RESET):{
                str.append("<book id=\"" + this.getID() + "\" reset=\"file\">");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                str.append("<files>");
                str.append(AnalyzerProperties.getPropertie("system_separator"));
                for (int i = 0; i < length; i++) {
                    File f = this.getFiles().get(i);
                    if (f.getID().equals("")) {
                        str.append("<file ui=\"" + uicontains.get("file") + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    } else {
                        str.append("<file id=\"" + f.getID() + "\" />");
                        str.append(AnalyzerProperties.getPropertie("system_separator"));
                    }
                }
                str.append("</files>");
                str.append("</book>");
                break;
            }
        }
        str.append("</update>");
        str.append(AnalyzerProperties.getPropertie("system_separator"));
        str.append("</request>");

        return str.toString();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fields write methods">
    private String writeAnnotations() {
        StringBuilder str = new StringBuilder();
        List<String> annotations = getAnnotations();
        int length = annotations.size();
        for (int i = 0; i < length; i++) {
            str.append("<annotation>");
            str.append(annotations.get(i));
            str.append("</annotation>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }

        return str.toString();
    }

    private String writeAnnotationsForRequest(String searchMethod) {
        StringBuilder str = new StringBuilder();
        List<String> annotations = getAnnotations();
        int length = annotations.size();
        for (int i = 0; i < length; i++) {
            str.append("<annotation type=\"");
            str.append(searchMethod);
            str.append("\">");
            str.append(annotations.get(i));
            str.append("</annotation>");
            str.append(AnalyzerProperties.getPropertie("system_separator"));
        }

        return str.toString();
    }
    //</editor-fold>

    public void addAuthor(Author author) {
        myAuthors.add(author);
    }

    public void addAnnotation(String annotation) {
        myAnnotations.add(annotation);
    }

    public void addFile(File file) {
        myFiles.add(file);
    }

    public static ArrayList<BookInfo> getBooksInfoFromRequest(String message) {
        ArrayList<BookInfo> out = new ArrayList<BookInfo>();
        //TODO: null check need because of server problem, need to remove after fix
        if (message.indexOf("<book") == -1 || message.indexOf("null") != -1) {
            Logger.setToErrorLog("Error in book information sent occured:" +
                    message);
            System.out.println("Error in book information sent occured:" +
                    message);
        }
        while (message.length() > 0) {
            message = message.substring(message.indexOf("<book"));
            out.add(getBookInfoFromRequest(message));
            message = message.substring("<book".length());
            message = message.substring(message.indexOf("<book"));
        }
        return out;
    }

    public void printInfo() {
        System.out.println("Title: " + this.getTitle().getName());
        List<Author> authors = this.getAuthors();
        int length = authors.size();
        System.out.println("Authors: ");
        for (int i = 0; i < length; i++) {
            System.out.println("    " + authors.get(i).getName());
        }
        List<File> files = this.getFiles();
        length = files.size();
        System.out.println("Files: ");
        for (int i = 0; i < length; i++) {
            System.out.print("    Link: " + files.get(i).getLink() + "; ");
            System.out.print("Size: " + files.get(i).getSize() + "; ");
            System.out.println("Type: " + files.get(i).getType());
            System.out.println("Book Cover: " + files.get(i).getImgLink());
        }
        System.out.println("Language: " + this.getLanguage().getShortDescriptions());
        List<String> annotations = this.getAnnotations();
        length = annotations.size();
        System.out.println("Annotations: ");
        for (int i = 0; i < length; i++) {
            System.out.println(annotations.get(i));
        }
    }

    private List<String> formAuthorsIDList() {
        List<String> out = new ArrayList<String>();
        String id;
        for (Author aut : myAuthors) {
            id = aut.getID();
            if (id != null) {
                out.add(id);
            }
        }
        return out;
    }
}
