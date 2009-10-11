package org.ebooksearchtool.client.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 01.10.2009
 * Time: 22:59:55
 * To change this template use File | Settings | File Templates.
 */
public class DataElement {

    private String[] myFields;

    public DataElement(){
        myFields = new String[5];
    }

    public String[] getFields() {
        return myFields;
    }
}
