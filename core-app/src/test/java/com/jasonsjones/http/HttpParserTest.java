package com.jasonsjones.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HttpParserTest {
    private HttpParser httpParser;

    @BeforeEach
    void setUp() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequestThrowsExceptionForInvalidRequestLine() throws IOException, HttpParsingException {
        String requestString = "GET /";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> {
            httpParser.parseHttpRequest(inputStream);
        });
    }

    @Test
    void parseHttpGetMethodFromValidRequestLine() throws IOException, HttpParsingException {
        String requestString = "GET / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        assertNotNull(httpRequest);
        assertEquals(HttpMethod.GET, httpRequest.getMethod());
    }
    
}
