package org.ebooksearchtool.crawler;

public abstract class AbstractLinksQueue {

	public abstract void offer(String s);
	
	public abstract String poll();
	
	public abstract boolean isEmpty();
	
}
