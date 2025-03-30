package com.jasonsjones.app.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.app.handlers.Handler;
import com.jasonsjones.app.handlers.RootHandler;

import com.jasonsjones.http.HttpParser;
import com.jasonsjones.http.HttpParsingException;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;

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
            handleClient(inputStream, outputStream);
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

    private void handleClient(InputStream inputStream, OutputStream outputStream) throws IOException, HttpParsingException {
        HttpRequest request = httpParser.parseHttpRequest(inputStream);
        HttpResponse response = new HttpResponse(outputStream);

        Handler handler = new RootHandler();
        handler.handle(request, response);
    } 
}
