package edu.escuelaing.app;

import edu.escuelaing.app.framework.WebApp;

public class App {

    public static void main(String[] args) {
        // Definir la carpeta donde estarán los archivos estáticos (HTML, CSS, JS, imágenes, etc.)
        SimpleHttpServer.staticfiles("src/main/resources/public");

        // Registrar rutas dinámicas
        WebApp.get("/hello", (req, resp) -> {
            String name = req.getValues("name");
            if (name == null || name.isEmpty()) {
                name = "World";
            }
            return "Hello " + name;
        });

        WebApp.get("/pi", (req, resp) -> String.valueOf(Math.PI));

        // Iniciar el servidor HTTP con try/catch
        try {
            SimpleHttpServer.main(args);
        } catch (Exception e) {
            System.err.println("Error iniciando el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
