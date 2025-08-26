package edu.escuelaing.app.framework;

import java.util.function.Function;

/**
 * Representa una ruta registrada en el framework (GET/POST -> handler).
 */
public class Route {
    private final String method;
    private final String path;
    private final Function<Request, Response> handler;

    public Route(String method, String path, Function<Request, Response> handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    public boolean matches(String method, String path) {
        return this.method.equalsIgnoreCase(method) && this.path.equals(path);
    }

    public Response handle(Request req) {
        return handler.apply(req);
    }
}
