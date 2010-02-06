package org.ebooksearchtool.client.model.books;

import java.util.Date;
import java.util.HashMap;

public class Book {

    private String myTitle;
    private String myID;
    private Author myAuthor;
    private String myLanguage;
    private Date myDate = new Date();
    private String myGenre;
    private String mySummary;
    private HashMap<String,  String> myLinks = new HashMap();
    private String myImage;

    public Book(){}

    public String getTitle() {
        return myTitle;
    }

    public void setTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    public Author getAuthor() {
        return myAuthor;
    }

    public void setAuthor(Author myAuthor) {
        this.myAuthor = myAuthor;
    }

    public String getLanguage() {
        return myLanguage;
    }

    public void setLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    public Date getDate() {
        return myDate;
    }

    public void setDate(String myDate) {
        this.myDate.setYear(Integer.parseInt(myDate));
    }

    public String getSummary() {
        return mySummary;
    }

    public void setSummary(String mySummary) {
        this.mySummary = mySummary;
    }

    public HashMap<String, String> getLinks() {
        return myLinks;
    }

    /*public String getPdfLink() {
        return myPdfLink;
    }

    public void setPdfLink(String myLink) {
        this.myPdfLink = myLink;
    } */

    public String getID() {
        return myID;
    }

    public void setID(String myID) {
        this.myID = myID;
    }

    public String getGenre() {
        return myGenre;
    }

    public void setGenre(String myGenre) {
        this.myGenre = myGenre;
    }

    /*public String getEpubLink() {
        return myEpubLink;
    }

    public void setEpubLink(String myEpubLink) {
        this.myEpubLink = myEpubLink;
    } */

    public String getImage() {
        return myImage;
    }

    public void setImage(String myImage) {
        this.myImage = myImage;
    }
}
