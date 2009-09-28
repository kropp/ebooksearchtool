package org.ebooksearchtool.crawler;

public interface AbstractRobotsExclusion {
	boolean isPermitted(String url);
}
