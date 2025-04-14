package com.jasonsjones.app.core;

import com.jasonsjones.app.config.Configuration;
import com.jasonsjones.app.config.ConfigurationManager;

public class Server {

    private final Configuration config;
    
    public Server(Configuration config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        this.config = config;
    }

    public Server() {
        this(ConfigurationManager.getInstance().getCurrentConfiguration());
    }

    public void start() {
        SocketListener socketListener = new SocketListener(this.config);
        socketListener.start();
    }
}
