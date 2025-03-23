package com.jasonsjones.http;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    CONNECTION("Connection");

    private final String NAME;

    HttpHeader(String headerName) {
        this.NAME = headerName;
    }

    public String getName() {
        return this.NAME;
    }
}
