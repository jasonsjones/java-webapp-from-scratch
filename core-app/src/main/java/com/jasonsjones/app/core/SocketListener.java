package com.jasonsjones.app.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.jasonsjones.app.config.Configuration;

public class SocketListener extends Thread {

    private int port;
        
    public SocketListener(Configuration config) {
        this.port = config.getPort();
    }

    @Override
    public void run() {
        System.out.println("Socket listenter running");
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {

            System.out.println("\nWeb server is running on port " + this.port);

            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket);
                connectionHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Problem occurred setting up socket" + e.getMessage());
        }
    }
}
