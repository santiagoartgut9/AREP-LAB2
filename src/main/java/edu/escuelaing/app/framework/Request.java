package edu.escuelaing.app.framework;

import edu.escuelaing.app.UrlUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa una petición HTTP entrante.
 */
public class Request {
    private final String method;
    private final String path;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers = new HashMap<>();

    public Request(String method, String path, Map<String, String> queryParams) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
    }

    public static Request of(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        // Primera línea: "GET /path?query HTTP/1.1"
        String line = reader.readLine();
        if (line == null || line.isEmpty()) {
            throw new IOException("Petición vacía");
        }
        String[] parts = line.split(" ");
        String method = parts[0];
        String fullPath = parts[1];

        String path = fullPath.split("\\?")[0];
        String query = fullPath.contains("?") ? fullPath.split("\\?", 2)[1] : "";

        // ❗ NO decodificamos aquí; lo hace UrlUtils.parseQuery(query)
        Map<String, String> queryParams = UrlUtils.parseQuery(query);

        Request req = new Request(method, path, queryParams);

        // Leer headers hasta línea vacía
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int idx = line.indexOf(":");
            if (idx != -1) {
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                req.headers.put(key, value);
            }
        }

        return req;
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public String getQueryParam(String key) { return queryParams.get(key); }

    // ✅ Alias solicitado
    public String getValues(String name) { return queryParams.get(name); }

    public Map<String, String> getHeaders() { return headers; }
}
