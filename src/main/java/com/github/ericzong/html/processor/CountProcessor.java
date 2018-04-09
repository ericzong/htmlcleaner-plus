package com.github.ericzong.html.processor;

import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;

public class CountProcessor extends AbstractProcessor {
    private long count = 0;

    @Override
    public boolean process(TagNode parentNode, HtmlNode htmlNode) {
        count++;

        return true;
    }

    public long getCount() {
        return count;
    }

    public void reset() {
        count = 0;
    }
}
