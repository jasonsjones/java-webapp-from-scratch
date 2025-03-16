package com.jasonsjones.app;

import com.jasonsjones.app.config.Configuration;
import com.jasonsjones.app.config.ConfigurationManager;
import com.jasonsjones.app.core.ServerListenerThread;

public class App {

    public static void main(String[] args) {
        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();
        ServerListenerThread listener = new ServerListenerThread(config);
        listener.start();
    }
}
