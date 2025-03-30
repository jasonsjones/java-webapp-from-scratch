package com.jasonsjones.app.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.app.handlers.Handler;
import com.jasonsjones.app.handlers.NotFoundHandler;
import com.jasonsjones.app.handlers.RootHandler;
import com.jasonsjones.http.HttpParser;
import com.jasonsjones.http.HttpParsingException;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;

public class ConnectionHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);
    
    private Socket socket;
    private HttpParser httpParser = new HttpParser();
    private Map<String, Handler> handlers = new HashMap<>();
    
    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        // Default handler for root path
        registerHandler("/", new RootHandler());
    }

    public void registerHandler(String path, Handler handler) {
        this.handlers.put(path, handler);
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
        String path = request.getUri().getPath();
        Handler handler = findHandler(path);
        handler.handle(request, response);
    } 

    private Handler findHandler(String requestPath) {
        // Exact match first
        if (handlers.containsKey(requestPath)) {
            return handlers.get(requestPath);
        }

        // Check for prefix matches (e.g., /api/*)
        for (Map.Entry<String, Handler> entry : handlers.entrySet()) {
            String registeredPath = entry.getKey();
            if (registeredPath.endsWith("*") && requestPath.startsWith(registeredPath.substring(0, registeredPath.length() - 1))) {
                return entry.getValue();
            }
        }

        return new NotFoundHandler();
    }
}
