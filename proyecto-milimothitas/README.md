# Proyecto Milimothitas

Este proyecto es una aplicación web full-stack que gestiona productos, ventas y usuarios. Consta de un backend desarrollado con Spring Boot (Java) y Maven, y un frontend estático (HTML, CSS, JavaScript) servido por el backend.

## Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado lo siguiente:

*   **Java Development Kit (JDK) 21 o superior:** Puedes descargarlo desde [OpenJDK](https://openjdk.java.net/install/index.html) o [Oracle JDK](https://www.oracle.com/java/technologies/downloads/).
*   **PostgreSQL (Opcional):** Si deseas utilizar una base de datos PostgreSQL persistente (recomendado para producción), deberás tener una instancia de PostgreSQL en tu sistema. Si no, la aplicación usará una base de datos en memoria H2 por defecto para desarrollo.

## Configuración y Ejecución

### 1. Clonar el Repositorio (si aún no lo has hecho)

```bash
git clone <URL_DEL_REPOSITORIO>
cd proyecto-milimothitas
```
*(Reemplaza `<URL_DEL_REPOSITORIO>` con la URL real de tu repositorio Git)*

### 2. Configuración de la Base de Datos (para PostgreSQL)

Si vas a usar PostgreSQL, sigue estos pasos:

*   **Crear una Base de Datos:** Abre tu cliente de PostgreSQL (por ejemplo, pgAdmin o psql) y crea una nueva base de datos, por ejemplo:
    ```sql
    CREATE DATABASE milimothitas_db;
    ```
*   **Actualizar Credenciales:**
    *   Asegúrate de que el archivo `src/main/resources/application.properties` (o `application.yml`) en tu proyecto tenga las credenciales correctas para tu base de datos PostgreSQL.
    *   Ejemplo (en `application.properties`):
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/milimothitas_db
        spring.datasource.username=tu_usuario_postgres
        spring.datasource.password=tu_contraseña_postgres
        spring.jpa.hibernate.ddl-auto=update # Esto actualizará el esquema de la DB automáticamente
        ```
    *   Si no quieres modificar el JAR, puedes colocar un archivo `application.properties` con la configuración de la base de datos en la misma carpeta que el archivo JAR ejecutable.

### 3. Construir el Proyecto (Backend)

Navega a la raíz del proyecto en tu terminal y ejecuta el siguiente comando para construir el archivo JAR ejecutable:

```bash
./mvnw clean install
```

Este comando compilará el proyecto y generará el archivo JAR en el directorio `target/`. El nombre del archivo será similar a `proyecto-milimothitas-0.0.1-SNAPSHOT.jar`.

### 4. Ejecutar la Aplicación

Una vez que el JAR ha sido construido, puedes ejecutar el backend desde la terminal:

```bash
java -jar target/proyecto-milimothitas-0.0.1-SNAPSHOT.jar
```

Asegúrate de estar en la raíz del proyecto cuando ejecutes este comando, o ajusta la ruta a `target/proyecto-milimothitas-0.0.1-SNAPSHOT.jar` si ejecutas desde otra ubicación.

### 5. Acceder al Frontend

Una vez que el backend esté en ejecución, la aplicación frontend estará disponible en tu navegador web. Abre tu navegador y ve a:

```
http://localhost:8080
```

*(Si la aplicación se inicia en un puerto diferente, ajusta la URL en consecuencia.)*

## Uso de la Aplicación

La aplicación te permitirá:

*   **Gestionar Productos:** Añadir, editar, ver y eliminar productos.
*   **Gestionar Usuarios:** Añadir, editar, ver y eliminar usuarios.
*   **Realizar Ventas:** Registrar nuevas ventas, añadir productos a las ventas y calcular totales.
*   **Ver Reportes de Ventas:** Consultar ventas realizadas y exportar un resumen en CSV.
*   **Visualizar Estadísticas:** Ver gráficos de ventas mensuales en el dashboard.

---

¡Disfruta usando la aplicación! 