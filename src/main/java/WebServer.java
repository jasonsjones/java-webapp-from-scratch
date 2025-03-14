package src.main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("\nWeb server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nNew connection from: " + clientSocket.getInetAddress() + "\n");
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Could not start server" + e.getMessage());
        }
    }


    private static void handleRequest(Socket clientSocket) throws IOException {
        echoRequest(clientSocket);

        String httpResponse = generateResponseHeader("text/html", "UTF-8") + generateResponseContent();
 
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(httpResponse.getBytes());
        outputStream.flush();
        clientSocket.close();
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
        String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + mimeType + "; charset=" + charset + "\r\n"
                + "Connection: close\r\n\r\n";

        return header;
    }

    private static String generateResponseContent() {
        String httpResponseContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head><title>Simple HTTP Server</title></head>"
                + "<body><h1>Hello, World!</h1></body>"
                + "</html>";

        return httpResponseContent;
    }
}
