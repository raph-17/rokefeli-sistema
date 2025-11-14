# Sistema de Ventas e Inventario - Colmenares Rokefeli

Este es el backend del sistema de ventas, inventario y gestión de pedidos para la empresa Colmenares Rokefeli.

## 🛠️ Tecnologías utilizadas
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- PostgreSQL
- Maven
- Lombok
- REST API  

## 📂 Estructura del proyecto
src/main/java/com/rokefeli/colmenares/api  
├── controller/  
├── service/  
├── repository/  
├── entity/  
├── dto/  
├── mapper/  
├── config/  
├── security/  
└── exception/  

## ⚙️ Configuración de Base de Datos
### 1. Crear base de datos en PostgreSQL:

CREATE DATABASE colmenares;

### 2. Configurar src/main/resources/application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/colmenares_rokefeli
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

Reemplazar TU_PASSWORD con tu contraseña real de PostgreSQL.  

## ▶️ Ejecución del proyecto

### Linux / Mac:
./mvnw spring-boot:run

### Windows:
mvnw spring-boot:run  

## 🧾 Estado del proyecto
Módulo	                    Estado  
Base del proyecto	          ✅ Completo  
Modelado BD	                ✅ Completo  
Entidades JPA               ✅ Siguiente paso  
Servicios y Controladores	  ⏳  
Seguridad JWT	              ⏳  
Frontend (Angular)	        Pendiente  


## 👤 Autor
Ricardo Plaza
Universidad Tecnológica del Perú — Proyecto académico
