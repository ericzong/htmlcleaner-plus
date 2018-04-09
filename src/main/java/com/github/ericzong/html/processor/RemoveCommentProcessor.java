package com.github.ericzong.html.processor;

import org.htmlcleaner.CommentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;

public class RemoveCommentProcessor extends AbstractProcessor {
    @Override
    public boolean process(TagNode parentNode, HtmlNode htmlNode) {
        if(htmlNode instanceof CommentNode) {
            parentNode.removeChild(htmlNode);

            return false;
        }

        return true;
    }
}
