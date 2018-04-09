package com.github.ericzong.html.processor;

import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;

public abstract class AbstractProcessor {
    private AbstractProcessor next;

    public final void invoke(TagNode parentNode, HtmlNode htmlNode) {
        boolean doNext = process(parentNode, htmlNode);

        if(doNext && next != null) {
            next.invoke(parentNode, htmlNode);
        }
    }

    public final AbstractProcessor add(AbstractProcessor next) {
        AbstractProcessor last = this;

        while (last.next != null) {
            last = last.next;
        }

        last.next = next;

        return this;
    }

    protected abstract boolean process(TagNode parentNode, HtmlNode htmlNode);
}
