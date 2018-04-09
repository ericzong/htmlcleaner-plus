package com.github.ericzong.html;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlSerializer;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HtmlUtils {

    public static TagNode getPageNode(URL url) throws IOException {
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode page = cleaner.clean(url);
        return page;
    }

    public static void exportPage(TagNode page, File outputFile) throws IOException {
        File dir = outputFile.getParentFile();
        if(!dir.exists()) {
            dir.mkdirs();
        }
        if(outputFile.exists()) {
            outputFile.delete();
        }

        CleanerProperties pros = new CleanerProperties();

        pros.setOmitXmlDeclaration(true);
        pros.setOmitComments(true);
        pros.setOmitCdataOutsideScriptAndStyle(true);

        HtmlSerializer serializer = new PrettyHtmlSerializer(pros);
        serializer.writeToFile(page, outputFile.getAbsolutePath());
    }
}
