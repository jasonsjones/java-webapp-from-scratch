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

public class ConnectionHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);
    private static final String CRLF = "\r\n";
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
            String httpResponse = handleRequest(request);
            
            LOGGER.info("Sending response:\n" + httpResponse);
            outputStream.write(httpResponse.getBytes());
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

    private String handleRequest(HttpRequest request) {
        return generateResponseHeader("text/html", "UTF-8") + generateResponseContent();
    }

    private String generateResponseHeader(String mimeType, String charset) {
        String header = "HTTP/1.1 200 OK" + CRLF 
                + "Content-Type: " + mimeType + "; charset=" + charset + CRLF
                + "Connection: close" + CRLF
                + CRLF;

        return header;
    }

    private String generateResponseContent() {
        String httpResponseContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head><title>Simple HTTP Server</title></head>"
                + "<body><h1>Hello, World!</h1></body>"
                + "</html>"
                + CRLF;

        return httpResponseContent;
    }

    
}
