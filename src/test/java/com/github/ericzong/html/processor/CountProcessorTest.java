package com.github.ericzong.html.processor;

import com.github.ericzong.TestUtils;
import com.github.ericzong.html.HtmlUtils;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class CountProcessorTest {
    private TagNode page;

    @BeforeTest
    public void setup() throws IOException {
        page = HtmlUtils.getPageNode(TestUtils.getDemoH5Url());
    }

    @Test
    public void getCount() {
        CountProcessor processor = new CountProcessor();
        page.traverse((TagNode parentNode, HtmlNode htmlNode) -> {
            processor.invoke(parentNode, htmlNode);

            return true;
        });
        Assert.assertEquals(processor.getCount(), 140);
    }
}
