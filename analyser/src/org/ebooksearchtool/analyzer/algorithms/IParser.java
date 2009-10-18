package org.ebooksearchtool.analyzer.algorithms;

import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public interface IParser {
    public BookInfo parse(String input);
}
