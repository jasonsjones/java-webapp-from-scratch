package com.jasonsjones.http;

public enum HttpStatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented");

    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int statusCode, String message) {
        this.STATUS_CODE = statusCode;
        this.MESSAGE = message;
    }
}
