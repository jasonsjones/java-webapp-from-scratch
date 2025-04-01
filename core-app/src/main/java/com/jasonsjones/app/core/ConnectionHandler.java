package com.jasonsjones.app.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonsjones.app.handlers.HandlerManager;
import com.jasonsjones.http.HttpParsingException;

public class ConnectionHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);
    
    private Socket socket;
    
    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        LOGGER.info("New connection from: " + this.socket.getInetAddress() + "\n");
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()) {
            HandlerManager handlerManager = new HandlerManager();
            handlerManager.handleClient(inputStream, outputStream);
        } catch (IOException | HttpParsingException e) {
            e.printStackTrace();
        } finally {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info("Connection closed");
        }
    }
}
