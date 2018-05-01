package com.github.ericzong;

import com.github.ericzong.css.CssUtils;
import com.github.ericzong.html.HtmlUtils;
import com.github.ericzong.html.processor.AbstractProcessor;
import com.github.ericzong.html.processor.ProcessorChainFactory;
import com.github.ericzong.html.processor.RemoveEmptyProcessor;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.File;

public class App {
    public static void main(String[] args) {
        clean(new File("e:\\test\\htmlclean\\"));
    }

    public static void clean(File dirFile) {
        File dir = dirFile;
        dir.listFiles(pathname -> {
            if(pathname.isFile() && pathname.getName().endsWith(".html")) {
                cleanHtml(pathname, dir);

                return true;
            }

            return false;
        });
    }

    private static void cleanHtml(File htmlFile, File dir) {
        try {
            TagNode page = HtmlUtils.getPageNode(htmlFile.toURI().toURL());
            CSSStyleSheet mergeSheet = CssUtils.mergeSheet(page);

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

            mergeSheet = CssUtils.cleanSheet(page, mergeSheet);

            TagNode styleNode = new TagNode("style");
            styleNode.addAttribute("type", "text/css");
            styleNode.addChild(new ContentNode(mergeSheet.toString()));
            Object[] headNodes = page.evaluateXPath("//head");
            TagNode headNode = (TagNode) headNodes[0];
            headNode.addChild(styleNode);

            File outputDir = new File(dir, "output");
            if(!outputDir.exists()) {
                outputDir.mkdir();
            }
            HtmlUtils.exportPage(page, new File(outputDir, htmlFile.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
