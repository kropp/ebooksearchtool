package ru.udalov.crawler.impl;

import java.util.*;
import ru.udalov.crawler.*;

public class LinksQueue implements AbstractLinksQueue {

	private final Queue<String> myQueue = new LinkedList<String>();
	
	public void offer(String s) {
		myQueue.offer(s);
	}
	public String poll() {
		return myQueue.poll();
	}
	public boolean isEmpty() {
		return myQueue.isEmpty();
	}
}
