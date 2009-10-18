package org.ebooksearchtool.analyzer.model;

import java.util.List;

/**
 * @author Алексей
 */

public class Author {
    private String myName;
    private List<String> myAliases;

    public Author(){
        myName = "Unknown author";
        myAliases = null;
    }
    
    public Author(String name){
        myName = name;
        myAliases = null;
    }
    
     public Author(String name, List<String> aliases){
        myName = name;
        myAliases = aliases;
    }

    /**
     * @return the myName
     */
    public String getName() {
        return myName;
    }

    /**
     * @param myName the myName to set
     */
    public void setName(String myName) {
        this.myName = myName;
    }

    /**
     * @return the myAliases
     */
    public List<String> getAliases() {
        return myAliases;
    }

    /**
     * @param myAliases the myAliases to set
     */
    public void setAliases(List<String> myAliases) {
        this.myAliases = myAliases;
    }    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Author other = (Author) obj;
        if ((this.myName == null) ? (other.myName != null) : !this.myName.equals(other.myName)) {
            return false;
        }
        if (this.myAliases != other.myAliases && (this.myAliases == null || !this.myAliases.equals(other.myAliases))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (this.myName != null ? this.myName.hashCode() : 0)
                + (this.myAliases != null ? this.myAliases.hashCode() : 0);
    }
}
