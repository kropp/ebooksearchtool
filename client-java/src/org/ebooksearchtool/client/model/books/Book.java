package org.ebooksearchtool.client.model.books;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Book {

    private String myTitle;
    private String mySubtitle;
    private String myID;
    private Author myAuthor;
    private String myLanguage;
    private String myPublisher;
    private Date myDate;
    private String myUpdateTime;
    private ArrayList<String> myGenre = new ArrayList<String>();
    private String myRights;
    private String mySummary;
    private String myContent;
    private HashMap<String,  String> myLinks = new HashMap<String, String>();
    private String myImage;
    private String mySource;

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

    public void setDate(String date) {
        if(myDate == null){
            myDate = new Date();
        }
        this.myDate.setYear(Integer.parseInt(date));
    }

    public String getUpdateTime() {
        return myUpdateTime;
    }

    public void setUpdateTime(String myDate) {
        this.myUpdateTime = myDate;
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

    public ArrayList<String> getGenre() {
        return myGenre;
    }

    public void setGenre(String genre) {
        myGenre.add(genre);
    }

    public String getImage() {
        return myImage;
    }

    public void setImage(String myImage) {
        this.myImage = myImage;
    }

    public String getContent() {
        return myContent;
    }

    public void setContent(String myContent) {
        this.myContent = myContent;
    }

    public String getPublisher() {
        return myPublisher;
    }

    public void setPublisher(String myPublisher) {
        this.myPublisher = myPublisher;
    }

    public String getRights() {
        return myRights;
    }

    public void setRights(String myRights) {
        this.myRights = myRights;
    }

    public String getSubtitle() {
        return mySubtitle;
    }

    public void setSubtitle(String mySubtitle) {
        this.mySubtitle = mySubtitle;
    }

    public void setSource(String mySource) {
        this.mySource = mySource;
    }

    public String getSource() {
        return mySource;
    }
}
