package Model;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 01.10.2009
 * Time: 20:08:37
 * To change this template use File | Settings | File Templates.
 */
public class Data {

    private String[] attributeNames;
    private Vector<DataElement> info;

    public Data(){

        attributeNames = new String[5];
        attributeNames[0] = "title";
        attributeNames[1] = "author";
        attributeNames[2] = "language";
        attributeNames[3] = "date";
        attributeNames[4] = "summary";

        info = new Vector<DataElement>();

    }

    public Vector<DataElement> getInfo() {
        return info;
    }

    public String[] getAttributes() {
        return attributeNames;
    }
}
