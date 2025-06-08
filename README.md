

<h1 align="center"># MilimothitasProject 1️⃣ <a href="https://github.com/acaycedo/Viaje_Interplanetario" target="blank">
Desarrollo</a></h1>
<h3 align="center">Desarrollo de Software Iberoamericana  &#127470;&#127475</h3>

<p align="left"> <img src="https://komarev.com/ghpvc/?username=100rabhcsmc&label=Profile%20views&color=0e75b6&style=flat" alt="DEVSENIOR" /> </p>

<a target="_blank" align="center">
  <img align="right" top="500" height="300" width="400" alt="GIF" src="https://media.giphy.com/media/SWoSkN6DxTszqIKEqv/giphy.gif">
</a>


- El desafío consistirá en desarrollar un programa centralizado en un módulo de ventas para la empresa Milimothitas el cual se centrará en un software escalable, lo cual facilitará la implementación de nuevos módulos y funcionalidades conforme la empresa crezca. Esto reducirá el riesgo de cancelación o bajo interés una vez se empiecen a ver resultados positivos. Además de asegurar una simplificación de todos los procesos que se llevan a cabo dentro de la empresa como las funciones de las embajadoras y de igual forma asegurar la escalabilidad y flexibilidad del sistema, el equipo adoptará diversas estrategias enfocadas en el desarrollo.
- 📝 Video de Presentación [Youtube](En Un Futuro)
<br/>

<h3 align="center" > <img src="https://media.giphy.com/media/iY8CRBdQXODJSCERIr/giphy.gif" width="30" height="30" style="margin-right: 10px;">Trabajo Colaborativo 🤝 </h3>
<br/>

### ESTRUCTURA 📂

# 🏪 Milimothitas - Sistema de Gestión de Ventas
Video de instalación
```
https://youtu.be/0HTtN5ZX2T0
```
Manual Tecnico:
```
  https://docs.google.com/document/d/1XoIpv13tFjzWQfctSFzpMuftk-t11yqwlrMJIzNU2B0/edit?usp=sharing
```
Pruebas:
```
  https://docs.google.com/spreadsheets/d/1D6bLa1fy18Wj64D2IUsprGvGBnr4TwDBgFq3LTKHIgA/edit?usp=sharing
```
## 📋 Descripción
Sistema de gestión de ventas desarrollado con Spring Boot y JavaScript vanilla, que permite administrar productos, realizar ventas y generar reportes.

## 🚀 Características Principales
- Gestión de productos (CRUD)
- Sistema de ventas
- Control de inventario
- Generación de reportes
- Exportación a CSV
- Interfaz responsiva

## 🛠️ Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven

### Frontend
- JavaScript Vanilla
- Bootstrap 5
- Axios
- Chart.js

## 📦 Estructura del Proyecto
```
proyecto-milimothitas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/milimothitas/proyecto/
│   │   │       ├── controllers/
│   │   │       ├── services/
│   │   │       ├── repositories/
│   │   │       ├── model/
│   │   │       │   ├── entities/
│   │   │       │   └── dto/
│   │   │       └── exceptions/
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── img/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/milimothitas/proyecto/
├── pom.xml
└── README.md
```

## 🚀 Instalación

### Requisitos Previos
- Java 17 o superior
- Maven
- PostgreSQL
- Node.js (opcional para desarrollo frontend)

### Pasos de Instalación
1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/proyecto-milimothitas.git
```

2. Configurar la base de datos
```sql
CREATE DATABASE milimothitas;
```

3. Configurar application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/milimothitas
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

4. Compilar el proyecto
```bash
./mvnw clean install
```

5. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

## 📝 API Documentation

### Endpoints Principales

#### Productos
- `GET /api/products` - Listar productos
- `POST /api/products` - Crear producto
- `PUT /api/products/{id}` - Actualizar producto
- `DELETE /api/products/{id}` - Eliminar producto

#### Ventas
- `GET /api/sales` - Listar ventas
- `POST /api/sales` - Crear venta
- `GET /api/sales/{id}` - Obtener venta por ID
- `GET /api/sales/filter-by-date` - Filtrar ventas por fecha

## 🧪 Pruebas

### Herramientas de Pruebas
- JUnit 5
- Mockito
- Spring Test
- H2 Database (pruebas)
- JaCoCo (cobertura)

### Tipos de Pruebas
1. **Pruebas Unitarias**
   - Servicios
   - Controladores
   - Repositorios

2. **Pruebas de Integración**
   - Flujos completos
   - API endpoints
   - Base de datos

3. **Pruebas de Usabilidad**
   - Interfaz de usuario
   - Navegación
   - Formularios

## 📊 Base de Datos

### Modelo Entidad-Relación
```
[Product] 1---* [SaleItem] *---1 [Sale]
```

### Tablas Principales
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    stock INTEGER NOT NULL,
    state BOOLEAN NOT NULL
);

CREATE TABLE sales (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fecha DATETIME NOT NULL,
    subtotal DOUBLE NOT NULL,
    iva DOUBLE NOT NULL,
    total_con_iva DOUBLE NOT NULL,
    descuento DOUBLE NOT NULL
);

CREATE TABLE sale_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price_at_sale DOUBLE NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```


## 📄 Licencia
Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para más detalles.


## 🔄 Actualizaciones Futuras
- [ ] Integración con pasarelas de pago

   
### INTEGRANTES UNIVERSIDAD IBEROAMERICANA
 - ANDRES FELIPE CAYCEDO JIMENEZ
 - VALENTINA RAMIREZ PINTO
