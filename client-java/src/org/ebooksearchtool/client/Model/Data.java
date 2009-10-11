package org.ebooksearchtool.client.Model;

import org.ebooksearchtool.client.Model.DataElement;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
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

    public List<DataElement> getInfo() {
        return Collections.unmodifiableList(myInfo);
    }

    public void setInfo(int elementIndex, int fieldIndex, String value){
        myInfo.get(elementIndex).getFields()[fieldIndex] = value;
    }

    public void addElement(DataElement addition){
        myInfo.add(addition);
    }

    public String[] getAttributes() {
        return myAttributeNames;
    }
}
