package com.github.ericzong;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestUtils {
    public static File getOutputFile(Class<?> testClass) throws IOException {
        URL url = testClass.getResource("/");
        File dir = new File(url.getFile(), "output");
        File outputFile = new File(dir, testClass.getSimpleName() + "-output.html");

        return outputFile;
    }

    public static URL getDemoH5Url() {
        return TestUtils.class.getResource(Constants.DEMO_H5_HTML_PATH);
    }

    public static URL getDemoCssUrl() {
        return TestUtils.class.getResource(Constants.DEMO_CSS_HTML_PATH);
    }

    public static URL getDemoInstanceUrl() {
        return TestUtils.class.getResource(Constants.DEMO_INSTANCE_HTML_PATH);
    }
}
