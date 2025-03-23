package com.jasonsjones.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class HttpMessage {
    protected HttpStatusCode statusCode;
    protected Map<String, String> headers = new HashMap<>();
    protected byte[] messageBody = new byte[0];

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public byte[] getMessageBody() {
        return this.messageBody;
    }

    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }
}
