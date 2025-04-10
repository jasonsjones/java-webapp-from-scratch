package com.jasonsjones.http;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CONNECTION("Connection");

    private final String NAME;

    HttpHeader(String headerName) {
        this.NAME = headerName;
    }

    public String getName() {
        return this.NAME;
    }
}
