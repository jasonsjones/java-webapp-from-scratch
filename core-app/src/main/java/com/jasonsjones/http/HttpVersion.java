package com.jasonsjones.http;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersion(String Literal, int Major, int Minor) {
        this.LITERAL = Literal;
        this.MAJOR = Major;
        this.MINOR = Minor;
    } 
}
