package com.jasonsjones.app.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.app.config.Configuration;

public class SocketListener extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketListener.class);

    private int port;
        
    public SocketListener(Configuration config) {
        this.port = config.getPort();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            LOGGER.info("Web server is running on port " + this.port);
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket);
                connectionHandler.start();
            }
        } catch (IOException e) {
            LOGGER.error("Problem occurred setting up socket" + e.getMessage());
        }
    }
}

