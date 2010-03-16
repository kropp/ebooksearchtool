package org.ebooksearchtool.crawler.cases;

import java.util.Collection;
import org.ebooksearchtool.crawler.Link;

public interface SpecialCase {
    
    void apply(Link pageLink, String page, Collection<Link> result);
    
}
