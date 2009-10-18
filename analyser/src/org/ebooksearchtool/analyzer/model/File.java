package org.ebooksearchtool.analyzer.model;

/**
 * @author Алексей
 */

public class File {
    private String myLink;
    private String mySize;
    private String myType;
    private String myMoreInfo;
    private String myImgLink;

    public File(){
        myLink = "Unknown link";
        mySize = "Unknown size";
        myType = "Unknown type";
        myMoreInfo = "Unknown info";
        myImgLink = "Unknown image link";
    }

    public File(String link, String size, String type, String moreInfo,
            String imageLink){
        myLink = link;
        mySize = size;
        myType = type;
        myMoreInfo = moreInfo;
        myImgLink = imageLink;
    }

    public File(String link){
        myLink = link;
        mySize = "Unknown size";
        myType = "Unknown type";
        myMoreInfo = "Unknown info";
        myImgLink = "Unknown image link";
    }

    /**
     * @return the myLink
     */
    public String getLink() {
        return myLink;
    }

    /**
     * @param myLink the myLink to set
     */
    public void setLink(String myLink) {
        this.myLink = myLink;
    }

    /**
     * @return the mySize
     */
    public String getSize() {
        return mySize;
    }

    /**
     * @param mySize the mySize to set
     */
    public void setSize(String mySize) {
        this.mySize = mySize;
    }

    /**
     * @return the myType
     */
    public String getType() {
        return myType;
    }

    /**
     * @param myType the myType to set
     */
    public void setType(String myType) {
        this.myType = myType;
    }

    /**
     * @return the myMoreInfo
     */
    public String getMoreInfo() {
        return myMoreInfo;
    }

    /**
     * @param myMoreInfo the myMoreInfo to set
     */
    public void setMoreInfo(String myMoreInfo) {
        this.myMoreInfo = myMoreInfo;
    }

    /**
     * @return the myImgLink
     */
    public String getImgLink() {
        return myImgLink;
    }

    /**
     * @param myImgLink the myImgLink to set
     */
    public void setImgLink(String myImgLink) {
        this.myImgLink = myImgLink;
    }
}
