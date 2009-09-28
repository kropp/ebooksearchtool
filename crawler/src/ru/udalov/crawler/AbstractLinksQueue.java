package ru.udalov.crawler;

public interface AbstractLinksQueue {
	void offer(String s);
	String poll();
	boolean isEmpty();
}
