package Logic.Parser;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 01.10.2009
 * Time: 20:34:00
 * To change this template use File | Settings | File Templates.
 */
public class Tag {

    private String tag;
    private boolean isEnabled;

    public Tag(String tagName){
        tag = tagName;
        isEnabled = false;
    }

    public boolean getStatus(){
        return isEnabled;
    }

    public void setStatus(boolean status){
        isEnabled = status;
    }

    public String getName(){
        return tag;
    }

}
