package org.ebooksearchtool.analyzer.model;

import org.ebooksearchtool.analyzer.model.SpecialWords.StringType;

/**
 * @author Aleksey Podolskiy
 */

public class Sentence {
    private String myInfo;
    private StringType myType;

    public Sentence(){
        myInfo = "";
        myType = StringType.word;
    }

    public Sentence(char info, StringType type){
        myInfo = info + "";
        myType = type;
    }

    public Sentence(String info, StringType type){
        myInfo = info;
        myType = type;
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
     * @return the myType
     */
    public StringType getType() {
        return myType;
    }

    /**
     * @param myType the myType to set
     */
    public void setType(StringType myType) {
        this.myType = myType;
    }

    public void clear(){
        this.myInfo = "";
        this.myType = StringType.word;
    }

    public void join(Sentence s){
        this.setInfo(this.getInfo() + s.getInfo());
        this.setType(StringType.word);
    }
}
