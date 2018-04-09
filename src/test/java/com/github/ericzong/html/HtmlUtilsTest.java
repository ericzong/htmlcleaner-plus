package com.github.ericzong.html;

import com.github.ericzong.TestUtils;
import org.htmlcleaner.TagNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class HtmlUtilsTest {
    private TagNode page;

    @Test
    public void getPageNode() throws IOException {
        page = HtmlUtils.getPageNode(TestUtils.getDemoH5Url());
        Assert.assertTrue(page instanceof TagNode);
    }

    @Test(dependsOnMethods = {"getPageNode"})
    public void exportPage() throws IOException {
        File outputFile = TestUtils.getOutputFile(this.getClass());
        HtmlUtils.exportPage(page, outputFile);
        System.out.println(outputFile.getAbsolutePath());
        Assert.assertTrue(outputFile.exists());
    }
}
