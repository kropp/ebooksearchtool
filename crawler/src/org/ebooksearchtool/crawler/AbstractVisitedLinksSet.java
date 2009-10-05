package org.ebooksearchtool.crawler;

import java.util.Collection;

public interface AbstractVisitedLinksSet {
	void add(String s);
	boolean contains(String s);
	boolean contains(Collection<? extends String> c);
	int size();
}
