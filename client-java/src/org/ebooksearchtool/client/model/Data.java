package org.ebooksearchtool.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: �������������
 * Date: 01.10.2009
 * Time: 20:08:37
 * To change this template use File | Settings | File Templates.
 */
public class Data {

    private String[] myAttributeNames = {
            "title",
            "author",
            "language",
            "date",
            "summary"
    };
    private List<DataElement> myInfo = new ArrayList<DataElement>();

    public Data(){}

    public void setBookTitle(int elementIndex, String value){
        myInfo.get(elementIndex).setTitle(value);
    }

    public void setBookAuthor(int elementIndex, String value){
        myInfo.get(elementIndex).setAuthor(value);
    }

    public void setBookLanguage(int elementIndex, String value){
        myInfo.get(elementIndex).setLanguage(value);
    }

    public void setBookDate(int elementIndex, String value){
        myInfo.get(elementIndex).setDate(value);
    }

    public void setBookSummary(int elementIndex, String value){
        myInfo.get(elementIndex).setSummary(value);
    }

    public void addElement(DataElement addition){
        myInfo.add(addition);
    }

    public List<DataElement> getInfo() {
        return Collections.unmodifiableList(myInfo);
    }

    public String[] getAttributes() {
        return myAttributeNames;
    }
}
