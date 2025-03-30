package com.jasonsjones.app.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jasonsjones.http.HttpHeader;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;
import com.jasonsjones.http.HttpStatusCode;
import com.jasonsjones.http.HttpVersion;
import com.jasonsjones.loaders.TemplateLoader;

public class RootHandler implements Handler{
    private static final String HOME_TEMPLATE = "templates/home.html";
    private static final String NOT_IMPLEMENTED_TEMPLATE = "templates/notImplemented.html";

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        switch(request.getMethod()) {
            case GET:
                handleGetRequest(request, response);
            default:
                handleNotImplemented(request, response);
        }
    }

    private void handleGetRequest(HttpRequest request, HttpResponse response) {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("heading", "Hello Java World!");
        String htmlContent = "";
        response.setVersion(HttpVersion.HTTP_1_1);;
        response.setStatusCode(HttpStatusCode.OK);
        response.addHeader(HttpHeader.CONTENT_TYPE.getName(), "text/html; charset= UTF-8");
        response.addHeader(HttpHeader.CONNECTION.getName(), "close");
        try {
            htmlContent = TemplateLoader.loadTemplate(HOME_TEMPLATE, templateData);
            response.setMessageBody(htmlContent.getBytes());
            response.getOutputStream().write(response.getResponseBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNotImplemented(HttpRequest request, HttpResponse response) {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("heading", "Server Error: Not Implemented");
        String htmlContent = "";
        response.setVersion(HttpVersion.HTTP_1_1);;
        response.setStatusCode(HttpStatusCode.OK);
        response.addHeader(HttpHeader.CONTENT_TYPE.getName(), "text/html; charset= UTF-8");
        response.addHeader(HttpHeader.CONNECTION.getName(), "close");
        try {
            htmlContent = TemplateLoader.loadTemplate(NOT_IMPLEMENTED_TEMPLATE, templateData);
            response.setMessageBody(htmlContent.getBytes());
            response.getOutputStream().write(response.getResponseBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
