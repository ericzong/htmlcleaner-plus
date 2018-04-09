package com.github.ericzong.test;

import com.github.ericzong.TestUtils;
import com.github.ericzong.css.CssUtils;
import com.github.ericzong.html.HtmlUtils;
import com.github.ericzong.html.processor.AbstractProcessor;
import com.github.ericzong.html.processor.ProcessorChainFactory;
import com.github.ericzong.html.processor.RemoveEmptyProcessor;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.IOException;

public class DemoInstanceTest {
    private TagNode page;
    private CSSStyleSheet mergeSheet;

    @BeforeTest
    public void setup() throws IOException {
        page = HtmlUtils.getPageNode(TestUtils.getDemoInstanceUrl());
    }

    @Test
    public void mergeSheets() throws XPatherException {
        mergeSheet = CssUtils.mergeSheet(page);
    }

    @Test(dependsOnMethods = "mergeSheets")
    public void cleanNormal() {
        AbstractProcessor processor = ProcessorChainFactory.getDefaultChain();
        page.traverse((parentNode, htmlNode) -> {
            processor.invoke(parentNode, htmlNode);

            return true;
        });

        RemoveEmptyProcessor removeEmptyProcessor = new RemoveEmptyProcessor();
        page.traverse(((parentNode, htmlNode) -> {
            removeEmptyProcessor.invoke(parentNode, htmlNode);

            return true;
        }));
    }

    @Test(dependsOnMethods = "cleanNormal")
    public void cleanMergeSheet() throws XPatherException {
        mergeSheet = CssUtils.cleanSheet(page, mergeSheet);
    }

    @Test(dependsOnMethods = "cleanMergeSheet")
    public void addMergeSheet() throws XPatherException {
        TagNode styleNode = new TagNode("style");
        styleNode.addAttribute("type", "text/css");
        styleNode.addChild(new ContentNode(mergeSheet.toString()));

        Object[] headNodes = page.evaluateXPath("//head");
        TagNode headNode = (TagNode) headNodes[0];
        headNode.addChild(styleNode);
    }

    @AfterTest
    public void teardown() throws IOException {
        HtmlUtils.exportPage(page, TestUtils.getOutputFile(this.getClass()));
    }
}
