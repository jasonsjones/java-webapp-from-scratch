package com.jasonsjones.http;

import java.util.Map;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private HttpUri uri;
    private HttpVersion version;
    private Map<String, String> headers;

    HttpRequest() {
        this.uri = new HttpUri();
    }

    public HttpUri getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(String method) {
        if (method.equals(HttpMethod.GET.toString())) {
            this.method = HttpMethod.GET;
        }
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (version.equals(HttpVersion.HTTP_1_1.LITERAL)) {
            this.version = HttpVersion.HTTP_1_1;
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
