package com.github.ericzong.html.processor;

import org.htmlcleaner.TagNode;

import java.util.Map;

public class RemoveTagByAttrProcessor extends RemoveTagProcessor {
    protected Map<String, String> attrValueMap;

    public RemoveTagByAttrProcessor(Map<String, String> attrValueMap) {
        this.attrValueMap = attrValueMap;
    }

    public RemoveTagByAttrProcessor(Map<String, String> attrValueMap, String... tags) {
        super(tags);
        this.attrValueMap = attrValueMap;
    }

    @Override
    protected boolean matchCondition(TagNode tagNode) {
        boolean isMatch = super.matchCondition(tagNode);

        if (isMatch) {
            for (Map.Entry<String, String> entry : attrValueMap.entrySet()) {
                String attr = entry.getKey();
                String value = entry.getValue();

                if (!tagNode.hasAttribute(attr) || !tagNode.getAttributeByName(attr).equals(value)) {
                    isMatch = false;
                    break;
                }
            }
        }

        return isMatch;
    }
}
