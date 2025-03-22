package com.jasonsjones.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void parseHttpRequestThrowsExceptionForEmptyRequestLine() throws IOException, HttpParsingException {
        String requestString = "";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> {
            httpParser.parseHttpRequest(inputStream);
        });
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
    void parseHttpRequestGetMethodFromValidRequestLine() throws IOException, HttpParsingException {
        String requestString = "GET / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        assertNotNull(httpRequest);
        assertEquals(HttpMethod.GET, httpRequest.getMethod());
    }

    @Test
    void parseHttpRequestNullMethodFromNonGetMethods() throws IOException, HttpParsingException {
        String requestString = "POST / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        assertNotNull(httpRequest);
        assertNull(httpRequest.getMethod());
    }

    @Test
    void parseHttpRequestUriFromValidRequestLine() throws IOException, HttpParsingException {
        String requestString = "GET /home HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        assertNotNull(httpRequest);
        assertEquals("/home", httpRequest.getUri().getPath());
        assertNull(httpRequest.getUri().getQueryParams());
    }

    @Test
    void parseHttpRequestUriQueryParamsFromValidRequestLine() throws IOException, HttpParsingException {
        String requestString = "GET /home?param1=value1&param2=value2 HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        assertNotNull(httpRequest);
        assertEquals("/home", httpRequest.getUri().getPath());
        assertEquals(2, httpRequest.getUri().getQueryParams().size());
        assertEquals("value1", httpRequest.getUri().getQueryParams().get("param1"));
    }

    @Test
    void parseHttpRequestThrowsWhenQueryParamsAreInvalid() throws IOException, HttpParsingException {
        String requestString = "GET /home?param1=value1&param2 HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        assertThrows(HttpParsingException.class, () -> {
            httpParser.parseHttpRequest(inputStream);
        });
    }

    @Test
    void parseHttpRequestVersionFromValidRequestLine() throws IOException, HttpParsingException {
        String requestString = "GET / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        assertNotNull(httpRequest);
        assertEquals(HttpVersion.HTTP_1_1, httpRequest.getVersion());
    }

    @Test
    void parseHttpRequestNullVersionFromUnknowVersion() throws IOException, HttpParsingException {
        String requestString = "GET / HTTP/1.2";
        InputStream inputStream = new ByteArrayInputStream(requestString.getBytes(StandardCharsets.US_ASCII));

        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        assertNotNull(httpRequest);
        assertNull(httpRequest.getVersion());
    }
    
    
}
