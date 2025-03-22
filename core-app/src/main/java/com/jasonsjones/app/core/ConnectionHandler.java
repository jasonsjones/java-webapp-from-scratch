package com.jasonsjones.app.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHandler extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);
    private static final String CRLF = "\r\n";
    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        super.run();
        LOGGER.info("New connection from: " + this.socket.getInetAddress() + "\n");
        try {
            handleRequest(socket);
        } catch (IOException e) {
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

    private static void handleRequest(Socket clientSocket) throws IOException {
        echoRequest(clientSocket);

        String httpResponse = generateResponseHeader("text/html", "UTF-8") + generateResponseContent();
        LOGGER.info("Sending response:\n" + httpResponse);
 
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(httpResponse.getBytes());
        outputStream.flush();
    }

    private static void echoRequest(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line;
        StringBuilder requestData = new StringBuilder();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestData.append(line).append(CRLF);
        }
        LOGGER.info("Received request:\n" + requestData);
    }

    private static String generateResponseHeader(String mimeType, String charset) {
        String header = "HTTP/1.1 200 OK" + CRLF 
                + "Content-Type: " + mimeType + "; charset=" + charset + CRLF
                + "Connection: close" + CRLF
                + CRLF;

        return header;
    }

    private static String generateResponseContent() {
        String httpResponseContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head><title>Simple HTTP Server</title></head>"
                + "<body><h1>Hello, World!</h1></body>"
                + "</html>"
                + CRLF;

        return httpResponseContent;
    }

    
}
