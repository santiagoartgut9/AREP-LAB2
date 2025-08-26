# 🖥️ AREP - Servidor HTTP y Mini Web Framework en Java

## 📌 Project Statement
**Web Framework Development for REST Services and Static File Management**

Este proyecto tiene como objetivo **transformar un servidor HTTP simple** en un **mini framework web** que permita el desarrollo de aplicaciones con **servicios REST** y la gestión de **archivos estáticos**.  

Con este framework, los desarrolladores podrán definir **endpoints usando funciones lambda**, **manejar parámetros de consulta (query params)** y **especificar la ubicación de archivos estáticos** para construir aplicaciones modernas en Java sin depender de frameworks externos como Spring o Spark.

---

##  Objective
El propósito del proyecto es:
- Entender y aplicar los principios de la **arquitectura cliente-servidor**.  
- Construir un **framework básico en Java** para definir servicios REST.  
- Aprender el manejo de **HTTP**, **protocolos de red** y la **organización de aplicaciones distribuidas**.  

---

##  Project Scope and Features

### 1. **GET Static Method for REST Services**
Permite definir endpoints REST usando funciones lambda:

```java
get("/hello", (req, res) -> "hello world!");
```

2. Query Value Extraction Mechanism

Mecanismo para extraer parámetros de consulta en la URL:
```bash
get("/hello", (req, res) -> "hello " + req.getValues("name"));
```
3. Static File Location Specification

Define la carpeta para servir archivos estáticos:
```bash
staticfiles("webroot/public");

```

4. Application Example

```bash
public static void main(String[] args) {
    staticfiles("/webroot");

    get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
    get("/pi", (req, resp) -> String.valueOf(Math.PI));
}
```

📂 Project Structure

```text

.
│   pom.xml
│
├───.vscode
│       settings.json
│
├───src
│   ├───main
│   │   ├───java
│   │   │   └───edu
│   │   │       └───escuelaing
│   │   │           └───app
│   │   │               ├── App.java              # Definición de endpoints
│   │   │               ├── MimeTypes.java        # Detección de tipos MIME
│   │   │               ├── SimpleHttpServer.java # Servidor HTTP principal
│   │   │               ├── UrlUtils.java         # Utilidad para parsear URLs
│   │   │               └───framework
│   │   │                   ├── Request.java
│   │   │                   ├── Response.java
│   │   │                   ├── Route.java
│   │   │                   └── WebApp.java       # Mini framework
│   │   └───resources
│   │       └───public
│   │           ├── index.html
│   │           ├── index2.html
│   │           ├── app.js
│   │           ├── styles.css
│   │           └───images/
│   │               ├── LOG.jpg
│   │               ├── LOGO.png
│   │               ├── LOGO2.jpg
│   │               ├── LOGO3.jpg
│   │               ├── UNI.jpg
│   │               ├── UNI2.jpg
│   │               └── header-logoescuela.jpg
│   └───test
│       └───java
│           └───edu
│               └───escuelaing
│                   └───app
│                       └── AppTest.java
└── target

```

⚙️ Requirements

Java 17+

Maven 3.8+

Git

Verifica versiones instaladas:
```bash
java -version
mvn -v
```
Execution

Clonar el repositorio:
```bash
git clone https://github.com/usuario/AREP.git
cd AREP
```

Compilar y empaquetar con Maven:
```bash
mvn clean install
```

Ejecutar el servidor:
```bash
mvn exec:java
```
Abrir en el navegador:
```bash
http://localhost:8080/
```

🌐 Available Endpoints
🔹 GET /hello

URL:
```bash
http://localhost:8080/hello?name=Pedro
```

Response:
Hello Pedro

🔹 GET /pi

URL:
```bash
http://localhost:8080/pi
```
Response:
3.141592653589793

🔹 POST /echo
```bash
URL: http://localhost:8080/echo
```

Body (raw text):
Hola Framework

Response:
You sent: Hola Framework

Framework

🔹 Static Files

Archivos estáticos servidos desde /public:
```bash
http://localhost:8080/index.html

http://localhost:8080/styles.css

http://localhost:8080/app.js

http://localhost:8080/images/LOGO.png
```

📦 Packaging and Deployment

Generar .jar ejecutable:
```bash
mvn package
```

Ejecutar con:
```bash
java -cp target/AREP-1.0.0.jar edu.escuelaing.app.SimpleHttpServer
```
🛠️ Technologies

Java 17 – Lenguaje principal

Maven – Gestión de dependencias y build

JUnit 4 – Testing framework
---
🎓 Outcome

Este proyecto dota a los desarrolladores de un mini-framework robusto para crear aplicaciones web con servicios REST y manejo de archivos estáticos. Además, fortalece la comprensión de:

Arquitectura HTTP y cliente-servidor

Arquitectura distribuida en aplicaciones modernas

Diseño de servicios web escalables y mantenibles
---
✒️ Author

Proyecto desarrollado por:
Santiago Arteaga 



