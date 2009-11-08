package org.ebooksearchtool.analyzer.model;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author Aleksey Podoplsky
 */

public class Field {
    private String myInfo;
    private HashMap<String,String> myParameters;

    public Field(){
        myInfo = "";
        myParameters = new HashMap<String,String>();
    }

    public Field(String info, HashMap<String, String> param){
        myInfo = info;
        myParameters = param;
    }

    /**
     * @return the myInfo
     */
    public String getInfo() {
        return myInfo;
    }

    /**
     * @param myInfo the myInfo to set
     */
    public void setInfo(String myInfo) {
        this.myInfo = myInfo;
    }

    /**
     * @return the myParameters
     */
    public HashMap<String, String> getParameters() {
        return myParameters;
    }

    /**
     * @param myParameters the myParameters to set
     */
    public void setParameters(HashMap<String, String> myParameters) {
        this.myParameters = myParameters;
    }

    public void setParameter(String paramName, String paramValue) {
        myParameters.put(paramName, paramValue);
    }

    public String getParameterByName(String paramName) {
        return myParameters.get(paramName);
    }
}
