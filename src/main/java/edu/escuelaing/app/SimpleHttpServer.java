package edu.escuelaing.app;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class SimpleHttpServer {

    private static final int PORT = 8080;
    private static final String ROOT = "src/main/resources/public";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor corriendo en http://localhost:" + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept(); // Manejo secuencial
            handleClient(clientSocket);
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream()
        ) {
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            StringTokenizer tokenizer = new StringTokenizer(requestLine);
            String method = tokenizer.nextToken();
            String path = tokenizer.nextToken();

            if (path.equals("/")) path = "/index.html";

            // REST endpoints
            if (path.startsWith("/hello")) {
                Map<String, String> params = UrlUtils.parseQuery(path);
                String name = params.getOrDefault("name", "Anon");
                sendResponse(out, "text/plain", ("Hello " + name + "!").getBytes());
                return;
            }

            if (path.startsWith("/hellopost")) {
                Map<String, String> params = UrlUtils.parseQuery(path);
                String name = params.getOrDefault("name", "Anon");
                sendResponse(out, "text/plain", ("Hello POST " + name + "!").getBytes());
                return;
            }

            // Archivos estáticos
            Path filePath = Paths.get(ROOT + UrlUtils.stripQuery(path));
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                String contentType = MimeTypes.getMimeType(filePath.toString());
                byte[] fileBytes = Files.readAllBytes(filePath);
                sendResponse(out, contentType, fileBytes);
            } else {
                sendResponse(out, "text/html", "<h1>404 Not Found</h1>".getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private static void sendResponse(OutputStream out, String contentType, byte[] content) throws IOException {
        out.write(("HTTP/1.1 200 OK\r\n").getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write(("Content-Length: " + content.length + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(content);
        out.flush();
    }
}
