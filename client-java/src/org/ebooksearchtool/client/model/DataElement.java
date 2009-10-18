package org.ebooksearchtool.client.model;

/**
 * Created by IntelliJ IDEA.
 * User: �������������
 * Date: 01.10.2009
 * Time: 22:59:55
 * To change this template use File | Settings | File Templates.
 */
public class DataElement {

    private String myTitle;
    private String myAuthor;
    private String myLanguage;
    private String myDate;
    private String mySummary;
    private String myLink;

    public DataElement(){}

    public String getTitle() {
        return myTitle;
    }

    public void setTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    public String getAuthor() {
        return myAuthor;
    }

    public void setAuthor(String myAuthor) {
        this.myAuthor = myAuthor;
    }

    public String getLanguage() {
        return myLanguage;
    }

    public void setLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    public String getDate() {
        return myDate;
    }

    public void setDate(String myDate) {
        this.myDate = myDate;
    }

    public String getSummary() {
        return mySummary;
    }

    public void setSummary(String mySummary) {
        this.mySummary = mySummary;
    }

    public String getLink() {
        return myLink;
    }

    public void setLink(String myLink) {
        this.myLink = myLink;
    }
}
