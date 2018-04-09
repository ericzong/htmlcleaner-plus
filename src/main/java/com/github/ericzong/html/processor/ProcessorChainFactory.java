package com.github.ericzong.html.processor;

import java.util.HashMap;

public class ProcessorChainFactory {

    public static AbstractProcessor getDefaultChain() {
        AbstractProcessor root = new CountProcessor();

        root.add(new RemoveCommentProcessor())
                .add(new RemoveTagProcessor("script", "iframe", "link", "base", "style"))
                .add(new RemoveTagByStyleProcessor(new HashMap<String, String>() {{put("display", "none");}}))
                ;

        return root;
    }

}
