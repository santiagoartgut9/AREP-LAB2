package edu.escuelaing.app;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {
    private static final Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("html", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
    }

    public static String getMimeType(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            return "text/plain";
        }
        String ext = filename.substring(dotIndex + 1).toLowerCase();
        return mimeTypes.getOrDefault(ext, "text/plain");
    }
}
