package ru.udalov.crawler;

public interface AbstractVisitedLinksSet {
	void add(String s);
	boolean contains(String s);
	int size();
}