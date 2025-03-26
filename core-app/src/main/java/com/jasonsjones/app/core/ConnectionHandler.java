package com.jasonsjones.app.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.http.HttpHeader;
import com.jasonsjones.http.HttpParser;
import com.jasonsjones.http.HttpParsingException;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;
import com.jasonsjones.http.HttpStatusCode;
import com.jasonsjones.http.HttpVersion;
import com.jasonsjones.loaders.TemplateLoader;

public class ConnectionHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);
    private static final String HOME_TEMPLATE = "templates/home.html";
    private static final String NOT_IMPLEMENTED_TEMPLATE = "templates/notImplemented.html";
    
    private Socket socket;
    private HttpParser httpParser = new HttpParser();

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        LOGGER.info("New connection from: " + this.socket.getInetAddress() + "\n");
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()) {

            HttpRequest request = httpParser.parseHttpRequest(inputStream);
            HttpResponse httpResponse = handleRequest(request);
            
            outputStream.write(httpResponse.getResponseBytes());
            outputStream.flush();

        } catch (IOException | HttpParsingException e) {
            e.printStackTrace();
        } finally {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info("Connection closed");
        }
    }

    private HttpResponse handleRequest(HttpRequest request) {
        switch(request.getMethod()) {
            case GET:
                return handleGetRequest(request);
            default:
                return handleNotImplemented(request);
        }
    }

    private HttpResponse handleGetRequest(HttpRequest request) {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("heading", "Hello Java World!");
        return generateResponse(HOME_TEMPLATE, HttpStatusCode.OK, templateData);
    }

    private HttpResponse handleNotImplemented(HttpRequest request) {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("heading", "Server Error: Not Implemented");
        return generateResponse(NOT_IMPLEMENTED_TEMPLATE, HttpStatusCode.NOT_IMPLEMENTED, templateData);
    }

    private HttpResponse generateResponse(String templateFile, HttpStatusCode statusCode, Map<String, String> templateData ) throws RuntimeException {
        try {
            String htmlContent = TemplateLoader.loadTemplate(templateFile, templateData);
            return generateResponse(statusCode, htmlContent.getBytes());
        } catch (IOException e) {
            LOGGER.error("Error loading HTML template file", e);
            throw new RuntimeException("Error loading html template: " + e.getMessage(), e);
        }
    }

    private HttpResponse generateResponse(HttpStatusCode statusCode, byte[] body) {
        HttpResponse.Builder builder = new HttpResponse.Builder()
            .withVersion(HttpVersion.HTTP_1_1)
            .withStatusCode(statusCode)
            .withHeader(HttpHeader.CONTENT_TYPE.getName(), "text/html; charset= UTF-8")
            .withHeader(HttpHeader.CONNECTION.getName(), "close")
            .withBody(body);
            return builder.build();
    }
}
