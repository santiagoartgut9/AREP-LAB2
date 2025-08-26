package edu.escuelaing.app.framework;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Representa una respuesta HTTP (mutable).
 */
public class Response {
    private int statusCode = 200;
    private String contentType = "text/plain";
    private String body = "";

    public Response() {}

    public Response(int statusCode, String contentType, String body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }

    // Getters
    public int getStatusCode() { return statusCode; }
    public String getContentType() { return contentType; }
    public String getBody() { return body; }

    // Setters
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setBody(String body) { this.body = body == null ? "" : body; }

    public void write(OutputStream out) throws IOException {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        String statusText;
        switch (statusCode) {
            case 200: statusText = "OK"; break;
            case 404: statusText = "Not Found"; break;
            case 400: statusText = "Bad Request"; break;
            case 500: statusText = "Internal Server Error"; break;
            default:  statusText = "OK";
        }

        String ct = contentType != null ? contentType : "text/plain";
        // Añadimos charset si no viene definido
        if (!ct.toLowerCase().contains("charset")) {
            ct = ct + "; charset=UTF-8";
        }

        out.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Type: " + ct + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("Content-Length: " + bodyBytes.length + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("Connection: close\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(("\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
        out.flush();
    }
}
