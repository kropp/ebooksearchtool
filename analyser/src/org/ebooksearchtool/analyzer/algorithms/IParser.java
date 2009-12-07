package org.ebooksearchtool.analyzer.algorithms;

import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Aleksey Podolskiy
 */

public interface IParser {
    public BookInfo parse(String input);
}
