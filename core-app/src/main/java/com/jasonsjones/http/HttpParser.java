package com.jasonsjones.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        HttpRequest request = new HttpRequest();

        parseRequestLine(reader, request);
        parseRequestHeaders(reader, request);

        return request;
    }

    private void parseRequestLine(BufferedReader reader, HttpRequest request) throws IOException, HttpParsingException {
        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new HttpParsingException("Request line is null");
        }
        String[] requestParts = requestLine.split(" ");
        if (requestParts.length != 3) {
            throw new HttpParsingException("Request line must have 3 parts");
        }

        LOGGER.info("Request line: {}", requestLine);
        request.setMethod(requestParts[0]);

        String uri = requestParts[1];
        String[] uriParts = uri.split("\\?");
        request.getUri().setPath(uriParts[0]);
        if (uriParts.length > 1) {
            request.getUri().setQueryParams(parseQueryString(uriParts[1]));
        }
        request.setVersion(requestParts[2]);

    }

    private void parseRequestHeaders(BufferedReader reader, HttpRequest request) throws IOException, HttpParsingException {
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(":", 2);
            if (headerParts.length == 2) {
                String key = headerParts[0].trim();
                String value = headerParts[1].trim();
                headers.put(key, value);
            } else {
                throw new HttpParsingException("Header must have 2 parts");
            }
        }
        request.setHeaders(headers);
    }

    private Map<String, String> parseQueryString(String queryString) throws HttpParsingException {
        Map<String, String> queryMap = new HashMap<>();

        String[] rawQueryParams = queryString.split("&");
        for (String rawQueryParam : rawQueryParams) {
            String[] queryParamParts = rawQueryParam.split("=");
            if (queryParamParts.length == 2) {
                String key = URLDecoder.decode(queryParamParts[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(queryParamParts[1], StandardCharsets.UTF_8);
                queryMap.put(key, value);
            } else {
                throw new HttpParsingException("Query parameter must have 2 parts");
            }
        }

        return queryMap;
    }
}
