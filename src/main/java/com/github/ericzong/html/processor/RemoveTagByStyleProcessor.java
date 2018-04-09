package com.github.ericzong.html.processor;

import org.htmlcleaner.TagNode;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveTagByStyleProcessor extends RemoveTagProcessor {
    protected Map<String, String> attrValueMap;

    public RemoveTagByStyleProcessor(Map<String, String> attrValueMap) {
        this.attrValueMap = attrValueMap;
    }

    public RemoveTagByStyleProcessor(Map<String, String> attrValueMap, String... tags) {
        super(tags);
        this.attrValueMap = attrValueMap;
    }

    @Override
    protected boolean matchCondition(TagNode tagNode) {
        boolean isMatch = super.matchCondition(tagNode);

        if(isMatch) {
            if(tagNode.hasAttribute("style")) {
                String style = tagNode.getAttributeByName("style");
                for (Map.Entry<String, String> entry : attrValueMap.entrySet()) {
                    String attr = entry.getKey();
                    String value = entry.getValue();

                    String regex = attr + "\\s*:\\s*" + value;
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(style);
                    if(!matcher.find()) {
                        isMatch = false;
                        break;
                    }
                }
            } else {
                isMatch = false;
            }
        }

        return isMatch;
    }
}
