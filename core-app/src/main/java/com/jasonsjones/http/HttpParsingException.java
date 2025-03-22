package com.jasonsjones.http;

public class HttpParsingException extends Exception {
    public HttpParsingException(String message) {
        super(message);
    }

    public HttpParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
