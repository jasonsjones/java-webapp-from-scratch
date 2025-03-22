package com.jasonsjones.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        HttpRequest request = new HttpRequest();

        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new HttpParsingException("Request line is null");
        }

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length != 3) {
            throw new HttpParsingException("Request line must have 3 parts");
        }

        LOGGER.info(requestLine);
        request.setMethod(requestParts[0]);

        return request;
    }
}
