package org.ebooksearchtool.analyzer.utils;

import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.Language;
import org.ebooksearchtool.analyzer.model.Title;
import java.util.List;

/**
 * @author Aleksey Podolskiy
 */

public class AlgorithmException extends Exception {

    public AlgorithmException(Title title, List<Author> author, String bookCover,
            Language language, String annotation) {        
    super(title.getName() + "<!>" + authorsToString(author) + "<!>" + bookCover +
            "<!>" + language.getShortDescriptions() + "<!>" + annotation);
    }

    private static String authorsToString(List<Author> author){
        StringBuilder authors = new StringBuilder();
        int length = author.size();
        for (int i = 0; i < length; i++) {
            authors.append(author.get(i).getName() + ";");
        }
        return authors.toString();
    }

}
