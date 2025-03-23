package com.jasonsjones.app.core;

import java.io.IOException;
import java.io.InputStream;

public class TemplateLoader {

    public static String loadTemplate(String templateFile, String heading) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(templateFile);
        if (inputStream == null) {
            throw new IOException("Template file not found: " + templateFile);
        }
        String htmlContent = new String(inputStream.readAllBytes());
        return htmlContent.replace("{{heading}}", heading);
    }
}
