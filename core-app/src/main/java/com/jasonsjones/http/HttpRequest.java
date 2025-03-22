package com.jasonsjones.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;

    HttpRequest() {
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
