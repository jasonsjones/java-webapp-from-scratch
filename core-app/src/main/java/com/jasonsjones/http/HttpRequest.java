package com.jasonsjones.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private HttpUri uri;

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
}
