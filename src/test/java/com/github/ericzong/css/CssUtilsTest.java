package com.github.ericzong.css;

import com.github.ericzong.TestUtils;
import com.github.ericzong.html.HtmlUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CssUtilsTest {
    @Test
    public void mergeStyleSheet() throws IOException, XPatherException {
        TagNode page = HtmlUtils.getPageNode(TestUtils.getDemoCssUrl());

        Object[] styleNodes = page.evaluateXPath("//style");
        String[] styleTexts = Arrays.stream(styleNodes).map(o -> ((TagNode) o).getText().toString()).collect(Collectors.toList()).toArray(new String[0]);
        List<CSSStyleSheet> styleSheets = CssUtils.getSheets(styleTexts);
        CSSStyleSheet mergeSheet = CssUtils.mergeSheets(styleSheets);

        int mergeLineCount = mergeSheet.toString().split("\r\n").length;

        System.out.println(mergeSheet.toString());

        Assert.assertEquals(mergeLineCount, 3);
    }

}
