package com.github.ericzong.html.processor;

import com.github.ericzong.TestUtils;
import com.github.ericzong.html.HtmlUtils;
import org.htmlcleaner.CommentNode;
import org.htmlcleaner.TagNode;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class RemoveCommentProcessorTest {
    private TagNode page;

    @BeforeTest
    public void setup() throws IOException {
        page = HtmlUtils.getPageNode(TestUtils.getDemoH5Url());
    }

    @Test
    public void removeComment() {
        RemoveCommentProcessor processor = new RemoveCommentProcessor();
        page.traverse((parentNode, htmlNode) -> {
            processor.invoke(parentNode, htmlNode);
            return true;
        });

        ThreadLocal<Boolean> isRemoveAll = new ThreadLocal<Boolean>() {
            @Override
            protected Boolean initialValue() {
                return Boolean.TRUE;
            }
        };
        page.traverse((parentNode, htmlNode) -> {
            if(htmlNode instanceof CommentNode) {
                isRemoveAll.set(Boolean.FALSE);
                return false;
            }

            return true;
        });

        Assert.assertTrue(isRemoveAll.get());
    }

//    @AfterTest
    public void teardown() throws IOException {
        File outputFile = TestUtils.getOutputFile(this.getClass());
        HtmlUtils.exportPage(page, outputFile);
        System.out.println(outputFile.getAbsolutePath());
    }
}
