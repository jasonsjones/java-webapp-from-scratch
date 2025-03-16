package com.jasonsjones.app.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.jasonsjones.app.config.Configuration;

public class ServerListenerThread extends Thread {

    private static final String CRLF = "/r/n";
    private int port;
        
    public ServerListenerThread(Configuration config) {
        this.port = config.getPort();
    }

    @Override
    public void run() {
        System.out.println("ServerListenerThread running");
        Socket clientSocket = null;
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("\nWeb server is running on port " + this.port);

            clientSocket = serverSocket.accept();
            System.out.println("\nNew connection from: " + clientSocket.getInetAddress() + "\n");
            handleRequest(clientSocket);
        } catch (IOException e) {
            System.err.println("Could not start server" + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        echoRequest(clientSocket);

        String httpResponse = generateResponseHeader("text/html", "UTF-8") + generateResponseContent();
 
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(httpResponse.getBytes());
        outputStream.flush();
    }

    private static void echoRequest(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line;
        StringBuilder requestData = new StringBuilder();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestData.append(line).append("\r\n");
        }
        System.out.println("Received request:\n" + requestData);
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
