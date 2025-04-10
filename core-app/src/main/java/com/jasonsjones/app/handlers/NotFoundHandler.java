package com.jasonsjones.app.handlers;

import java.io.IOException;

import com.jasonsjones.http.HttpHeader;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;
import com.jasonsjones.http.HttpStatusCode;
import com.jasonsjones.http.HttpVersion;
import com.jasonsjones.loaders.TemplateLoader;

public class NotFoundHandler implements Handler {
    private static final String NOT_FOUND_TEMPLATE = "templates/notfound.html";

    @Override
    public boolean handle(HttpRequest request, HttpResponse response) {
        response.setVersion(HttpVersion.HTTP_1_1);;
        response.setStatusCode(HttpStatusCode.NOT_FOUND);
        response.addHeader(HttpHeader.CONTENT_TYPE.getName(), "text/html; charset= UTF-8");
        response.addHeader(HttpHeader.CONNECTION.getName(), "close");
        String htmlContent = "";
        try {
            htmlContent = TemplateLoader.loadTemplate(NOT_FOUND_TEMPLATE, null);
            response.setMessageBody(htmlContent.getBytes());
            response.getOutputStream().write(response.getResponseBytes());
            response.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
