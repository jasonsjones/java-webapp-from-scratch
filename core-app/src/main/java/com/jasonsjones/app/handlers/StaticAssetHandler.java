package com.jasonsjones.app.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.http.HttpHeader;
import com.jasonsjones.http.HttpMethod;
import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;
import com.jasonsjones.http.HttpStatusCode;
import com.jasonsjones.http.HttpVersion;

public class StaticAssetHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticAssetHandler.class);
    // Define the base directory for static assets within your resources
    private static final String STATIC_ASSETS_BASE_PATH = "assets";
    // Simple map for common MIME types
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("css", "text/css; charset=UTF-8");
        MIME_TYPES.put("js", "application/javascript; charset=UTF-8");
        MIME_TYPES.put("html", "text/html; charset=UTF-8");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("ico", "image/x-icon");
        // Add more MIME types as needed
    }

    @Override
    public boolean handle(HttpRequest request, HttpResponse response) {

        if (request.getMethod().equals(HttpMethod.GET)) {
            return handleGetRequest(request, response);
        }

        return false;
    }

    private boolean handleGetRequest(HttpRequest request, HttpResponse response) {
        String requestedPath = request.getUri().getPath();
        
        // Basic path sanitization to prevent directory traversal
        if (requestedPath.contains("..")) {
            LOGGER.warn("Attempted directory traversal: {}", requestedPath);
            return handleNotFound(request, response);
        }

        if (requestedPath.startsWith("/" + STATIC_ASSETS_BASE_PATH)) {
            LOGGER.info("Attempting to serve static asset: {}", requestedPath);
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream resourceStream = classLoader.getResourceAsStream("templates" + requestedPath);
                if (resourceStream != null) {
                    byte[] fileBytes = resourceStream.readAllBytes();
                    resourceStream.close();
                    String contentType = determineContentType(requestedPath);

                    response.setVersion(HttpVersion.HTTP_1_1);
                    response.setStatusCode(HttpStatusCode.OK);
                    response.addHeader(HttpHeader.CONTENT_TYPE.getName(), contentType);
                    response.addHeader(HttpHeader.CONTENT_LENGTH.getName(), String.valueOf(fileBytes.length));
                    response.addHeader(HttpHeader.CONNECTION.getName(), "close"); // Or keep-alive if supported
                    response.setMessageBody(fileBytes);

                    response.getOutputStream().write(response.getResponseBytes());
                    response.getOutputStream().flush();
                    LOGGER.info("Successfully served static asset: {}", requestedPath);
                } else {
                    return handleNotFound(request, response);
                }
            } catch (IOException e) {
                LOGGER.error("Error serving static asset: {}", requestedPath, e);
            }
        }

        return false;
    }

    private boolean handleNotFound(HttpRequest request, HttpResponse response) {
        response.setVersion(HttpVersion.HTTP_1_1);;
        response.setStatusCode(HttpStatusCode.NOT_FOUND);
        response.addHeader(HttpHeader.CONNECTION.getName(), "close");
        return false;
    }

    private String determineContentType(String path) {
        String extension = "";
        int i = path.lastIndexOf('.');
        if (i > 0) {
            extension = path.substring(i + 1).toLowerCase();
        }
        return MIME_TYPES.getOrDefault(extension, "application/octet-stream"); // Default binary type
    }
}
