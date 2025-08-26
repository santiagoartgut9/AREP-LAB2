package edu.escuelaing.app;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {
    /**
     * Parsea la query string en un mapa clave-valor,
     * decodificando espacios, acentos y caracteres especiales.
     * Ej: "name=Juan+P%C3%A9rez&msg=hola%20mundo"
     */
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return params;
        }
        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=", 2);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1
                    ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                    : "";
            params.put(key, value);
        }
        return params;
    }
}
