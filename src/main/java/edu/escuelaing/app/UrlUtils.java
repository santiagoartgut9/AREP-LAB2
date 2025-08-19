package edu.escuelaing.app;

import java.util.HashMap;
import java.util.Map;

public class UrlUtils {
    public static Map<String, String> parseQuery(String path) {
        Map<String, String> params = new HashMap<>();
        int idx = path.indexOf('?');
        if (idx != -1) {
            String query = path.substring(idx + 1);
            for (String pair : query.split("&")) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    public static String stripQuery(String path) {
        int idx = path.indexOf('?');
        return idx == -1 ? path : path.substring(0, idx);
    }
}
