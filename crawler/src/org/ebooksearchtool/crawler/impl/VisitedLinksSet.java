package org.ebooksearchtool.crawler.impl;

import java.util.*;
import org.ebooksearchtool.crawler.*;

public class VisitedLinksSet implements AbstractVisitedLinksSet {

	private final Set<String> mySet = new HashSet<String>();
	
	public void add(String s) {
		mySet.add(s);
	}
	public boolean contains(String s) {
		return mySet.contains(s);
	}
	/** returns true iff at least one element of the collection is present in the set */
	public boolean contains(Collection<? extends String> c) {
		for (String s : c) {
			if (contains(s)) {
				return true;
			}
		}
		return false;
	}
	public int size() {
		return mySet.size();
	}
}
