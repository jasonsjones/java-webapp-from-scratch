package com.jasonsjones.http;

import java.util.Map;

public class HttpUri {
    private String path;
    private Map<String, String> queryParams;

    public HttpUri() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }
}
