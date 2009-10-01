package Logic.Parser;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 01.10.2009
 * Time: 20:32:55
 * To change this template use File | Settings | File Templates.
 */
public class BookTags {

    private Tag[] tags;

    public BookTags(){
        tags = new Tag[5];
        tags[0] = new Tag("title");
        tags[1] = new Tag("author");
        tags[2] = new Tag("dc:language");
        tags[3] = new Tag("dc:issued");
        tags[4] = new Tag("summary");
    }

    public Tag[] getTags(){
        return tags;
    }

}
