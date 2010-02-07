package org.ebooksearchtool.client.model;

import org.ebooksearchtool.client.model.books.Data;

/**
 * Created by IntelliJ IDEA.
 * User: Виктор
 * Date: 07.02.2010
 * Time: 19:11:05
 * To change this template use File | Settings | File Templates.
 */
public class QueryAnswer {

    Data myData;
    String myNextPage;
    int myTotalBooksNumber;

    public QueryAnswer(){
        myData = new Data();
        myNextPage = new String();
    }

    public String getNextPage(){
        return myNextPage;
    }

    public void setNextPage(String page){
        myNextPage = page;
    }

    public int getTotalBooksNumber(){
        return myTotalBooksNumber;
    }

    public void setTotalBooksNumber(int num){
        myTotalBooksNumber = num;
    }

    public Data getData(){
        return myData;
    }

}
