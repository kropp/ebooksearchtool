package org.ebooksearchtool.client.model.books;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 19.04.2010
 * Time: 14:19:47
 * To change this template use File | Settings | File Templates.
 */
public class Shelf {

    List<Book> myBooks = new ArrayList<Book>();

    public List<Book> getBooks() {
        return Collections.unmodifiableList(myBooks);
    }

}
