package com.jasonsjones.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TemplateLoader {

    public static String loadTemplate(String templateFile, Map<String, String> templateData) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(templateFile);
        if (inputStream == null) {
            throw new IOException("Template file not found: " + templateFile);
        }
        String htmlContent = new String(inputStream.readAllBytes());
        return interpolateData(htmlContent, templateData);
    }

    private static String interpolateData(String htmlContent, Map<String, String> templateData) {
        for (Map.Entry<String, String> entry : templateData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            htmlContent = htmlContent.replace("{{" + key + "}}", value);
        }
        return htmlContent;
    }
}
