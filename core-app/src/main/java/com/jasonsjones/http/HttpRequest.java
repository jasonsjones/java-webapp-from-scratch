package com.jasonsjones.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private HttpUri uri;
    private HttpVersion version;

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
}
