package com.github.ericzong.html.processor;

import com.github.ericzong.TestUtils;
import com.github.ericzong.html.HtmlUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

public class RemoveTagByAttrProcessorTest {
    private TagNode page;

    @BeforeTest
    public void setup() throws IOException {
        page = HtmlUtils.getPageNode(TestUtils.getDemoH5Url());
    }

    @Test
    public void removeTagByAttr() throws XPatherException {
        Assert.assertTrue(page.evaluateXPath("//*[@href='#']").length > 0);

        RemoveTagByAttrProcessor processor = new RemoveTagByAttrProcessor(new HashMap<String, String>() {{
            put("href", "#");
        }});
        page.traverse((parentNode, htmlNode) -> {
            processor.invoke(parentNode, htmlNode);
            return true;
        });

        Assert.assertTrue(page.evaluateXPath("//*[@href='#']").length == 0);
    }
}
