# FitMarket - Marketplace de Suplementos Deportivos

**Materia:** API - Aplicaciones Interactivas (UADE)
**Grupo:** 5
**Cuatrimestre:** 1C 2026

## Descripcion

FitMarket es un e-commerce de suplementos deportivos y accesorios de gimnasio. Permite a vendedores publicar productos y gestionar stock/precios/descuentos, y a compradores navegar el catalogo, gestionar un carrito de compras persistido en el servidor, y realizar compras.

## Stack Tecnologico

- **Backend:** Spring Boot 4.0.4, Java 17
- **Base de datos:** MySQL 8 (Docker)
- **Seguridad:** Spring Security + JWT (JJWT 0.12.6)
- **ORM:** Spring Data JPA + Hibernate
- **Documentacion API:** Swagger (SpringDoc OpenAPI)
- **Frontend:** React + Redux Toolkit + Tailwind CSS

## Requisitos Previos

- Java 17+
- Maven 3.9+
- Docker Desktop
- Node.js 18+ (para el frontend)

## Instalacion y Ejecucion

### 1. Clonar el repositorio

```bash
git clone https://github.com/grupo5-uade/marketplace.git
cd marketplace
```

### 2. Levantar MySQL con Docker

```bash
docker run --name fitmarket-db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=fitmarket \
  -p 3306:3306 \
  -d mysql:8
```

### 3. Crear las tablas e insertar datos semilla

```bash
docker exec -i fitmarket-db mysql -uroot -proot fitmarket < schema.sql
```

### 4. Configurar application.properties

Copiar el archivo de ejemplo y ajustar si es necesario:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### 5. Ejecutar el backend

```bash
./mvnw spring-boot:run
```

El servidor levanta en `http://localhost:8080`

### 6. Verificar

- **API:** http://localhost:8080/status
- **Swagger:** http://localhost:8080/swagger-ui.html

## Usuarios de Prueba

| Usuario | Password | Rol |
|---------|----------|-----|
| vendedor1 | 123456 | SELLER |
| comprador1 | 123456 | BUYER |

> **Nota:** Las passwords estan hasheadas con BCrypt en la base de datos.

## Endpoints Principales

### Publicos (sin autenticacion)

| Metodo | URL | Descripcion |
|--------|-----|-------------|
| POST | `/auth/register` | Registrar usuario |
| POST | `/auth/login` | Login (devuelve JWT) |
| GET | `/status` | Health check |
| GET | `/products` | Listar productos |
| GET | `/products/{id}` | Detalle de producto |
| GET | `/categories` | Listar categorias |

### Requieren autenticacion (Bearer Token)

| Metodo | URL | Rol | Descripcion |
|--------|-----|-----|-------------|
| POST | `/products` | SELLER | Crear producto |
| PUT | `/products/{id}` | SELLER | Modificar producto |
| DELETE | `/products/{id}` | SELLER | Eliminar producto |
| PATCH | `/products/{id}/stock` | SELLER | Actualizar stock |
| PATCH | `/products/{id}/discount` | SELLER | Aplicar descuento |
| GET | `/favorites` | AUTH | Listar favoritos |
| POST | `/favorites/{productId}` | AUTH | Agregar a favoritos |
| DELETE | `/favorites/{productId}` | AUTH | Quitar de favoritos |
| GET | `/favorites/{productId}/check` | AUTH | Verificar si es favorito |
| GET | `/cart?userId={id}` | AUTH | Ver carrito |
| POST | `/cart/items` | AUTH | Agregar al carrito |
| PUT | `/cart/items/{id}` | AUTH | Modificar cantidad |
| DELETE | `/cart/items/{id}` | AUTH | Eliminar del carrito |
| POST | `/orders/checkout` | AUTH | Confirmar compra |
| GET | `/orders?userId={id}` | AUTH | Listar ordenes |
| GET | `/orders/{id}` | AUTH | Detalle de orden |
| GET | `/users/{id}` | AUTH | Ver perfil |
| PUT | `/users/{id}` | AUTH | Actualizar perfil |

## Flujo de Autenticacion

1. `POST /auth/login` con username y password
2. Recibir `access_token` (JWT)
3. Incluir en cada request: `Authorization: Bearer <token>`
4. El token expira en 24 horas

## Arquitectura

```
Request HTTP
    |
    v
[JwtAuthenticationFilter] --> Extrae y valida Bearer token
    |
    v
[SecurityFilterChain] --> Verifica permisos por rol (BUYER/SELLER)
    |
    v
[Controller] --> Recibe request
    |
    v
[Service] --> Logica de negocio
    |
    v
[Repository (JPA)] --> Acceso a datos
    |
    v
[MySQL] --> Persistencia
```

## Pruebas con Postman

Importar la coleccion `FitMarket.postman_collection.json` incluida en el proyecto. Contiene requests pre-configurados para:

- Registro y login
- CRUD de productos (con token)
- Carrito de compras
- Checkout y ordenes
- Favoritos (agregar, listar, verificar, quitar)
- Pruebas de 401 (sin token) y 403 (rol insuficiente)

## Estructura del Proyecto

```
com.grupo5.tpo.marketplace/
├── config/          SecurityConfig
├── security/        JwtUtil, JwtAuthFilter, CustomUserDetailsService
├── controller/      REST Controllers (7)
├── service/         Logica de negocio (6)
├── repository/      JPA Repositories (8)
├── model/           Entidades JPA (8 + 2 enums)
├── dto/             DTOs de request/response
└── exception/       Manejo de errores
```
