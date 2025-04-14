package com.jasonsjones.app.config;

public class Configuration {

    private int port;
    private String webRoot;

    public Configuration(int port, String webRoot) {
        setPort(port);
        this.webRoot = webRoot;
    }

    public int getPort() {
        return port;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
