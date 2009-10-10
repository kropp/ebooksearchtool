package org.ebooksearchtool.crawler;

import java.util.Collection;

public interface AbstractVisitedLinksSet {
	void add(String s);
	boolean contains(String s);
	/* returns true iff at least one element of the collection is present in the set */
	boolean contains(Collection<? extends String> c);
	int size();
}
