package com.github.ericzong.html.processor;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class RemoveEmptyProcessor extends AbstractProcessor {
    @Override
    protected boolean process(TagNode parentNode, HtmlNode htmlNode) {
        try {
            if (isEmptyNode(htmlNode)) {
                parentNode.removeChild(htmlNode);

                TagNode parent = parentNode;
                while(isEmptyNode(parent)) {
                    parent.removeFromTree();
                    parent = parent.getParent();
                }

                return false;
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean isEmptyNode(HtmlNode node) throws XPatherException {
        if (node instanceof TagNode) {
            TagNode tagNode = (TagNode) node;
            String text = deleteEmptyChar(tagNode.getText().toString());
            if (!"img".equalsIgnoreCase(tagNode.getName())
                    && text.isEmpty()
                    && tagNode.evaluateXPath(".//img").length == 0) {
                return true;
            }
        } else if (node instanceof ContentNode) {
            ContentNode contentNode = (ContentNode) node;
            String text = deleteEmptyChar(contentNode.getContent());
            if (contentNode.isBlank() || text.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private String deleteEmptyChar(String text) {
        String str = text;
        str = str.replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("\t", "")
                .replace("　", "")
                .replace(" ", "");

        return str;
    }
}
