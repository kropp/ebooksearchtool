package ru.udalov.crawler.impl;

import java.util.*;
import ru.udalov.crawler.*;

public class VisitedLinksSet implements AbstractVisitedLinksSet {

	private final Set<String> mySet = new HashSet<String>();
	
	public void add(String s) {
		mySet.add(s);
	}
	public boolean contains(String s) {
		return mySet.contains(s);
	}
	public int size() {
		return mySet.size();
	}
}
