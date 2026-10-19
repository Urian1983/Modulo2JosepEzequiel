# Módulo 2 – API REST de Profesores y Materias

API REST desarrollada con **Spring Boot** y **JPA/Hibernate** para gestionar profesores y las materias que imparten, con persistencia en **MySQL**.

**Autores:** Josep y Ezequiel

---

## Tecnologías

| Tecnología | Detalle |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Web MVC | `spring-boot-starter-webmvc` |
| Spring Data JPA (Hibernate) | `spring-boot-starter-data-jpa` |
| Base de datos | MySQL 8 (`mysql-connector-j`) |
| Build | Maven (incluye Maven Wrapper) |

## Estructura del proyecto

```
Modulo2JosepEzequiel/
├── modulo2.sql                      # Script de la base de datos (estructura + datos)
├── .env.example                     # Plantilla de credenciales locales
├── pom.xml
├── mvnw / mvnw.cmd                  # Maven Wrapper
└── src/
    ├── main/
    │   ├── java/com/JosepEzequiel/Modulo2Inserta/
    │   │   ├── Modulo2InsertaApplication.java   # Clase principal
    │   │   ├── entity/                          # Entidades JPA
    │   │   │   ├── Profesor.java
    │   │   │   └── Materia.java
    │   │   ├── dao/                             # Capa de acceso a datos
    │   │   │   ├── ProfesorDAO.java / ProfesorDAOImpl.java
    │   │   │   └── MateriaDAO.java  / MateriaDAOImpl.java
    │   │   └── rest/                            # Controladores REST
    │   │       ├── ProfesorRestController.java
    │   │       └── MateriaRestController.java
    │   └── resources/application.properties
    └── test/java/.../Modulo2InsertaApplicationTests.java
```

La aplicación sigue una arquitectura por capas: **Controller (rest) → DAO → EntityManager → MySQL**.

## Modelo de datos

Relación **uno a muchos**: un `Profesor` puede impartir varias `Materia`s, y cada `Materia` tiene como máximo un profesor.

### Tabla `profesor`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | BIGINT (PK, auto-increment) | Identificador |
| `nombre` | VARCHAR(255) | Nombre |
| `apellidos` | VARCHAR(255) | Apellidos |
| `direccion` | VARCHAR(255) | Dirección |

### Tabla `materias`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | INT (PK, auto-increment) | Identificador |
| `nombre` | VARCHAR(255) | Nombre de la materia |
| `num_creditos` | INT | Número de créditos |
| `horas_credito` | INT | Horas por crédito |
| `id_profesor` | BIGINT (FK → `profesor.id`) | Profesor asignado (opcional) |

El script `modulo2.sql` incluye tres materias de ejemplo: *Bases de Datos*, *Cálculo Diferencial* y *Algebra*.

## Requisitos previos

- JDK 21
- MySQL 8 en ejecución
- Maven (opcional, el proyecto incluye `mvnw`)

## Puesta en marcha

### 1. Clonar el repositorio

```bash
git clone https://github.com/Urian1983/Modulo2JosepEzequiel.git
cd Modulo2JosepEzequiel
```

### 2. Crear la base de datos

```bash
mysql -u root -p -e "CREATE DATABASE modulo2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p modulo2 < modulo2.sql
```

### 3. Configurar las credenciales

Las credenciales de la base de datos **no se suben al repositorio**. Cada desarrollador las define en un archivo `.env` local, en la raíz del proyecto (junto al `pom.xml`). Copia la plantilla y rellénala con tus datos:

```bash
cp .env.example .env
```

```properties
DB_URL=jdbc:mysql://localhost:3306/modulo2
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
```

El archivo `application.properties` lee esas variables e importa el `.env` automáticamente:

```properties
spring.config.import=optional:file:.env[.properties]
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

La API quedará disponible en `http://localhost:8080/api`.

## Endpoints

Todas las rutas cuelgan del prefijo `/api`.

### Profesores

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/profesores` | Lista todos los profesores |
| `GET` | `/api/profesores/{profesorId}` | Obtiene un profesor por id |
| `POST` | `/api/profesores` | Crea un profesor (`201 Created`) |
| `PUT` | `/api/profesores` | Actualiza un profesor |
| `DELETE` | `/api/profesores/{id}` | Elimina un profesor (`204 No Content`, o `404` si no existe) |

**Ejemplo de cuerpo (`POST` / `PUT`):**

```json
{
  "nombre": "Ana",
  "apellidos": "García",
  "direccion": "Calle Mayor 1"
}
```

### Materias

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/materias` | Lista todas las materias |
| `GET` | `/api/materias/{id}` | Obtiene una materia por id (`404` si no existe) |
| `POST` | `/api/materias` | Crea una materia (`201 Created`, `409 Conflict` si el id ya existe) |
| `PUT` | `/api/materias` | Actualiza una materia (`404` si no existe) |
| `DELETE` | `/api/materias/{id}` | Elimina una materia (`204 No Content`) |
| `GET` | `/api/materia/{materiaId}/profesor` | Devuelve el profesor asignado a una materia (`404` si no tiene) |

**Ejemplo de cuerpo (`POST` / `PUT`):**

```json
{
  "nombre": "Estadística",
  "numCreditos": 6,
  "horasCredito": 90
}
```

Para asignar un profesor a una materia, incluye su id en el cuerpo:

```json
{
  "id": 2,
  "nombre": "Bases de Datos",
  "numCreditos": 4,
  "horasCredito": 60,
  "profesor": { "id": 1 }
}
```

### Ejemplos con `curl`

```bash
# Listar materias
curl http://localhost:8080/api/materias

# Crear un profesor
curl -X POST http://localhost:8080/api/profesores \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana","apellidos":"García","direccion":"Av. Diagonal 100"}'

# Obtener una materia
curl http://localhost:8080/api/materias/2

# Profesor de una materia
curl http://localhost:8080/api/materia/2/profesor
```

## Tests

```bash
./mvnw test
```

Los tests necesitan MySQL en marcha y el archivo `.env` configurado.
