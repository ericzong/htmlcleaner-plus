package com.github.ericzong.html.processor;

import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RemoveTagProcessor extends AbstractProcessor {
    protected Set<String> tags = new HashSet<>();

    protected RemoveTagProcessor() {
    }

    public RemoveTagProcessor(String... tags) {
        if(tags != null && tags.length > 0) {
            Arrays.stream(tags).forEach(t -> this.tags.add(t.toLowerCase()));
        }
    }

    @Override
    public boolean process(TagNode parentNode, HtmlNode htmlNode) {
        if(htmlNode instanceof TagNode) {
            TagNode tagNode = (TagNode) htmlNode;
            if(matchCondition(tagNode)) {
                (tagNode).removeFromTree();
                return false;
            }
        }

        return true;
    }

    protected boolean matchCondition(TagNode tagNode) {
        String tag = tagNode.getName().toLowerCase();

        return tags.isEmpty() || tags.contains(tag);
    }
}
