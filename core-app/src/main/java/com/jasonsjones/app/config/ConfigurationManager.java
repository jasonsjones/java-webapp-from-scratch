package com.jasonsjones.app.config;

public class ConfigurationManager {

    private static Configuration currentConfiguration;
    private static Configuration defaultConfiguration = new Configuration(8080, "");

    private static ConfigurationManager configManager;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (configManager == null) {
            configManager = new ConfigurationManager();
        }
        return configManager;
    }

    public void loadConfigurationFile(String filePath) {
        // Not yet implemented
    }

    public Configuration getCurrentConfiguration() {
        if (currentConfiguration == null) {
           return defaultConfiguration;
        }
        return currentConfiguration;
    }

    public void setCurrentConfiguration(Configuration currentConfig) {
        currentConfiguration = currentConfig;
    }

    
}
