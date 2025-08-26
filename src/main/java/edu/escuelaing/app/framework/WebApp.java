package edu.escuelaing.app.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.BiFunction;

/**
 * Mini framework que administra rutas y sus handlers.
 */
public class WebApp {
    private static final List<Route> routes = new ArrayList<>();

    // Versión básica: recibe un handler que retorna un Response
    public static void get(String path, Function<Request, Response> handler) {
        routes.add(new Route("GET", path, handler));
    }

    public static void post(String path, Function<Request, Response> handler) {
        routes.add(new Route("POST", path, handler));
    }

    // ✅ Overload: handler que retorna String (estilo get("/hello", (req, res) -> "hola"))
    public static void get(String path, BiFunction<Request, Response, String> handler) {
        routes.add(new Route("GET", path, req -> {
            Response res = new Response();                  // 200, text/plain por defecto
            String body = handler.apply(req, res);
            res.setBody(body == null ? "" : body);          // permite que el handler modifique status/type
            return res;
        }));
    }

    /**
     * Busca la ruta y ejecuta su handler.
     * @return Response si encontró ruta; null si no hay coincidencia.
     */
    public static Response handle(Request req) {
        // 🔎 Normalizar path (quitar query params si vienen incluidos)
        String cleanPath = req.getPath();
        if (cleanPath.contains("?")) {
            cleanPath = cleanPath.substring(0, cleanPath.indexOf("?"));
        }

        for (Route r : routes) {
            if (r.matches(req.getMethod(), cleanPath)) {
                return r.handle(req);
            }
        }
        return null; // Permite que el servidor sirva archivos estáticos
    }
}
