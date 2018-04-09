package com.github.ericzong.html.processor;

import com.github.ericzong.TestUtils;
import com.github.ericzong.html.HtmlUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveTagByStyleProcessorTest {
    private TagNode page;

    @BeforeTest
    public void setup() throws IOException {
        page = HtmlUtils.getPageNode(TestUtils.getDemoH5Url());
    }

    @Test
    public void removeTagByStyle() throws XPatherException, IOException {
        Assert.assertTrue(getCount() > 0);

        RemoveTagByStyleProcessor processor = new RemoveTagByStyleProcessor(new HashMap<String, String>() {{put("color", "red");}});
        page.traverse((parentNode, htmlNode) -> {
            processor.invoke(parentNode, htmlNode);

            return true;
        });

        Assert.assertTrue(getCount() == 0);
    }

    private long getCount() throws XPatherException {
        long count = Arrays.stream(page.evaluateXPath("//*[@style]")).filter(o -> {
            TagNode tagNode = (TagNode) o;
            String style = tagNode.getAttributeByName("style");
            String regex = "color\\s*:\\s*red";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(style);
            return matcher.find();
        }).count();

        return count;
    }
}
