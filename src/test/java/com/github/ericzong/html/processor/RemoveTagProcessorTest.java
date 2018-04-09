package com.github.ericzong.html.processor;

import com.github.ericzong.TestUtils;
import com.github.ericzong.html.HtmlUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class RemoveTagProcessorTest {
    private TagNode page;

    @BeforeTest
    public void setup() throws IOException {
        page = HtmlUtils.getPageNode(TestUtils.getDemoH5Url());
    }

    @Test
    public void removeTags() throws XPatherException {
        Assert.assertTrue(page.evaluateXPath("//script").length > 0);
        Assert.assertTrue(page.evaluateXPath("//li").length > 0);

        RemoveTagProcessor processor = new RemoveTagProcessor("script", "li");
        page.traverse((parentNode, htmlNode) -> {
            processor.invoke(parentNode, htmlNode);
            return true;
        });

        Assert.assertTrue(page.evaluateXPath("//script").length == 0);
        Assert.assertTrue(page.evaluateXPath("//li").length == 0);
    }

}
