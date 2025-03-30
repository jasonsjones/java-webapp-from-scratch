package com.jasonsjones.app.handlers;

import com.jasonsjones.http.HttpRequest;
import com.jasonsjones.http.HttpResponse;

public interface Handler {
    void handle(HttpRequest request, HttpResponse response);
}
