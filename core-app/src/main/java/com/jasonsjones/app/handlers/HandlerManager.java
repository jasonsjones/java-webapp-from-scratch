package com.jasonsjones.app.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.http.HttpParser;
import com.jasonsjones.http.HttpParsingException;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;

public class HandlerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerManager.class);

    private HttpParser httpParser = new HttpParser();
    private Map<String, Handler> handlers = new HashMap<>();

    public HandlerManager() {
        initialize();
    }

    public void handleClient(InputStream inputStream, OutputStream outputStream) throws IOException, HttpParsingException {
        HttpRequest request = httpParser.parseHttpRequest(inputStream);
        HttpResponse response = new HttpResponse(outputStream);
        String path = request.getUri().getPath();
        Handler handler = findHandler(path);
        handler.handle(request, response);
    } 

    public void registerHandler(String path, Handler handler) {
        LOGGER.info("Registering handler for path: " + path);
        this.handlers.put(path, handler);
    }

    public Handler findHandler(String requestPath) {
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

        LOGGER.warn("No handler found for path: " + requestPath);
        return new NotFoundHandler();
    }

    private void initialize() {
        // Register default handlers
        registerHandler("/", new RootHandler());
    }
}
