package com.jasonsjones.app.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.http.HttpParser;
import com.jasonsjones.http.HttpParsingException;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;
import com.jasonsjones.http.HttpStatusCode;
import com.jasonsjones.http.HttpVersion;

public class ConnectionHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);
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
        HttpResponse.Builder builder = new HttpResponse.Builder()
            .withVersion(HttpVersion.HTTP_1_1)
            .withStatusCode(HttpStatusCode.OK)
            .withHeader("Content-Type", "text/html; charset= UTF-8")
            .withHeader("Connection", "close")
            .withBody(generateResponseContent("Hello java world!").getBytes());
            return builder.build();
    }

    private HttpResponse handleNotImplemented(HttpRequest request) {
        HttpResponse.Builder builder = new HttpResponse.Builder()
            .withVersion(HttpVersion.HTTP_1_1)
            .withStatusCode(HttpStatusCode.NOT_IMPLEMENTED)
            .withHeader("Content-Type", "text/html; charset= UTF-8")
            .withHeader("Connection", "close")
            .withBody(generateResponseContent("Server Error: Not Implemented").getBytes());
            return builder.build();
    }

    private String generateResponseContent(String title) {
        String httpResponseContent = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head><title>Simple HTTP Server</title></head>\n"
                + "<body><h1>" + title + "</h1></body>\n"
                + "</html>\n";

        return httpResponseContent;
    }

    
}
