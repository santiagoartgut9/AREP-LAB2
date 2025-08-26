package edu.escuelaing.app;

import edu.escuelaing.app.framework.Request;
import edu.escuelaing.app.framework.Response;
import edu.escuelaing.app.framework.WebApp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleHttpServer {

    private static int PORT = 8080;

    /**
     * Carpeta de archivos estáticos por defecto (modo desarrollo).
     * Puedes cambiarla en tiempo de ejecución con {@link #staticfiles(String)}.
     */
    private static String STATIC_ROOT = "src/main/resources/public";

    /**
     * Permite definir la ubicación de archivos estáticos como lo pide el proyecto.
     * Ejemplos:
     *  - staticfiles("/webroot")          -> target/classes/webroot
     *  - staticfiles("webroot/public")    -> target/classes/webroot/public
     *  - staticfiles("src/main/resources/public") (ruta absoluta/relativa tal cual)
     */
    public static void staticfiles(String location) {
        if (location == null || location.isEmpty()) return;

        // Normaliza: quita '/' inicial si existe
        String normalized = location.startsWith("/") ? location.substring(1) : location;

        // Si la ruta apunta a recursos empacados, se resuelve a target/classes/<ruta>
        // (coincide con lo pedido en el enunciado)
        if (!normalized.startsWith("src/") && !normalized.startsWith("target/")) {
            STATIC_ROOT = Paths.get("target", "classes", normalized).toString();
        } else {
            // Permitir rutas explícitas
            STATIC_ROOT = normalized;
        }
        System.out.println("[StaticFiles] Sirviendo estáticos desde: " + STATIC_ROOT);
    }

    /**
     * Permite cambiar el puerto si lo necesitas.
     */
    public static void setPort(int port) {
        PORT = port;
    }

    public static void main(String[] args) throws IOException {
        // TIP: si quieres usar el modo empaquetado:
        // SimpleHttpServer.staticfiles("/webroot"); // -> target/classes/webroot
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor corriendo en http://localhost:" + PORT);
        System.out.println("[StaticFiles] Directorio actual: " + STATIC_ROOT);

        while (true) {
            Socket clientSocket = serverSocket.accept(); // Manejo secuencial (no concurrente)
            handleClient(clientSocket);
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            var in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream()
        ) {
            // 1) Construir Request desde el socket
            Request req;
            try {
                req = Request.of(in);
            } catch (IOException ex) {
                byte[] body = "Bad Request".getBytes(StandardCharsets.UTF_8);
                sendResponse(out, 400, "text/plain", body);
                return;
            }

            // 2) Intentar resolver como ruta REST del framework
            Response res = WebApp.handle(req);
            if (res != null) {
                res.write(out);
                return;
            }

            // 3) Si no hubo ruta, servir archivos estáticos
            String path = req.getPath();
            if ("/".equals(path)) path = "/index.html";

            Path filePath = Paths.get(STATIC_ROOT + path);
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                String contentType = MimeTypes.getMimeType(filePath.toString());
                byte[] fileBytes = Files.readAllBytes(filePath);
                sendResponse(out, 200, contentType, fileBytes);
            } else {
                byte[] body = "<h1>404 Not Found</h1>".getBytes(StandardCharsets.UTF_8);
                sendResponse(out, 404, "text/html", body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private static void sendResponse(OutputStream out, int statusCode, String contentType, byte[] content) throws IOException {
        String statusText;
        switch (statusCode) {
            case 200: statusText = "OK"; break;
            case 404: statusText = "Not Found"; break;
            case 400: statusText = "Bad Request"; break;
            case 500: statusText = "Internal Server Error"; break;
            default:  statusText = "OK";
        }
        out.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Type: " + contentType + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Length: " + content.length + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("Connection: close\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(content);
        out.flush();
    }
}
