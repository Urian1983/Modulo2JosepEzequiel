# Módulo 2 – API REST de Profesores y Materias

API REST desarrollada con **Spring Boot**, **JPA/Hibernate** y **Spring Security** para gestionar profesores y las materias que imparten, con persistencia en **MySQL**. Incluye autenticación HTTP Basic con usuarios almacenados en base de datos, control de acceso por roles y despliegue con **Docker Compose**.

**Autores:** Josep y Ezequiel

---

## Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Modelo de datos](#modelo-de-datos)
- [Seguridad y roles](#seguridad-y-roles)
- [Requisitos previos](#requisitos-previos)
- [Configuración](#configuración)
- [Puesta en marcha](#puesta-en-marcha)
- [Endpoints](#endpoints)
- [Tests](#tests)
- [Limitaciones conocidas](#limitaciones-conocidas)

---

## Tecnologías

| Tecnología | Detalle |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Web MVC | `spring-boot-starter-webmvc` |
| Spring Data JPA (Hibernate) | `spring-boot-starter-data-jpa` |
| Spring Security | `spring-boot-starter-security` (HTTP Basic + `JdbcUserDetailsManager`) |
| Spring JDBC | `spring-boot-starter-jdbc` |
| Base de datos | MySQL 8 (`mysql-connector-j`) |
| Build | Maven (incluye Maven Wrapper) |
| Contenedores | Docker (build multietapa) + Docker Compose |

## Estructura del proyecto

```
Modulo2JosepEzequiel/
├── .env.example                 # Plantilla de credenciales para ejecución local
├── Dockerfile                   # Build multietapa (JDK 21 → JRE 21)
├── docker-compose.yaml          # Servicios: mysql + app
├── pom.xml
├── mvnw / mvnw.cmd              # Maven Wrapper
└── src/
    ├── main/
    │   ├── java/com/JosepEzequiel/Modulo2Inserta/
    │   │   ├── Modulo2InsertaApplication.java   # Clase principal
    │   │   ├── entity/                          # Entidades JPA y roles
    │   │   │   ├── Profesor.java
    │   │   │   ├── Materia.java
    │   │   │   └── Roles.java                   # Enum: ALUMNO, PROFESOR, RECTOR
    │   │   ├── dao/                             # Capa de acceso a datos
    │   │   │   ├── ProfesorDAO.java / ProfesorDAOImpl.java
    │   │   │   └── MateriaDAO.java  / MateriaDAOImpl.java
    │   │   ├── rest/                            # Controladores REST
    │   │   │   ├── ProfesorRestController.java
    │   │   │   └── MateriaRestController.java
    │   │   └── security/
    │   │       └── SecurityConfig.java          # Reglas de acceso por rol
    │   └── resources/
    │       ├── application.properties
    │       └── schema.sql                       # Esquema + usuarios iniciales
    └── test/java/.../Modulo2InsertaApplicationTests.java
```

Arquitectura por capas: **Controller (rest) → DAO → EntityManager → MySQL**, con **Spring Security** filtrando las peticiones antes de llegar a los controladores.

## Modelo de datos

Relación **uno a muchos**: un `Profesor` puede impartir varias `Materia`s, y cada `Materia` tiene como máximo un profesor.

### Tabla `profesor`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | INT (PK, auto-increment) | Identificador |
| `nombre` | VARCHAR(255) | Nombre |
| `apellidos` | VARCHAR(255) | Apellidos |
| `direccion` | VARCHAR(255) | Dirección |

### Tabla `materias`

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | INT (PK, auto-increment) | Identificador |
| `nombre` | VARCHAR(255), NOT NULL | Nombre de la materia |
| `num_creditos` | INT | Número de créditos |
| `horas_credito` | INT | Horas por crédito |
| `id_profesor` | INT (FK → `profesor.id`) | Profesor asignado (opcional) |

### Tablas de seguridad (`JdbcUserDetailsManager`)

| Tabla | Columnas | Descripción |
|---|---|---|
| `users` | `username` (PK), `password`, `enabled` | Usuarios de la API |
| `authorities` | `username` (FK → `users`), `authority` | Roles de cada usuario (`ROLE_*`) |

## Seguridad y roles

La API usa **HTTP Basic**. Todas las rutas requieren autenticación; los permisos dependen del rol:

| Operación | RECTOR | PROFESOR | ALUMNO |
|---|:---:|:---:|:---:|
| `GET` (profesores, materias y sus relaciones) | ✅ | ✅ | ✅ |
| `PUT` (actualizar profesor o materia) | ✅ | ✅ | ❌ |
| `POST` (crear profesor o materia) | ✅ | ❌ | ❌ |
| `DELETE` (eliminar profesor o materia) | ✅ | ❌ | ❌ |

### Usuarios de ejemplo

`schema.sql` crea tres usuarios de prueba:

| Usuario | Contraseña | Rol |
|---|---|---|
| `rector` | `1234` | `ROLE_RECTOR` |
| `profesor` | `1234` | `ROLE_PROFESOR` |
| `alumno` | `1234` | `ROLE_ALUMNO` |

> ⚠️ Las contraseñas se guardan con el prefijo `{noop}` (texto plano). Es una decisión consciente para el entorno académico/de desarrollo; **no debe usarse así en producción** (usar `{bcrypt}` o similar). CSRF está deshabilitado al ser una API sin estado de navegador.

## Requisitos previos

**Opción A – Docker (recomendada):**

- Docker y Docker Compose

**Opción B – Ejecución local:**

- JDK 21
- MySQL 8 en ejecución con una base de datos llamada `modulo2` (el instalado en tu máquina o el del contenedor de Docker)
- Maven (opcional, el proyecto incluye `mvnw`)

## Configuración

La aplicación lee la conexión a la base de datos de tres variables: `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`.

- **Con Docker Compose** no hay que crear ningún `.env`: el propio `docker-compose.yaml` ya se las pasa al contenedor de la app.
- **Ejecutando en local** (IDE o `mvnw`) se leen de un fichero `.env` en la raíz del proyecto, que **no se sube al repositorio** (está en `.gitignore`). Copia la plantilla y ajústala:

```bash
cp .env.example .env
```

```properties
# MySQL instalado en tu máquina (puerto 3306):
DB_URL=jdbc:mysql://localhost:3306/modulo2
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

# O bien el MySQL del contenedor de Docker (puerto 3307, usuario root sin contraseña):
# DB_URL=jdbc:mysql://localhost:3307/modulo2
# DB_USERNAME=root
# DB_PASSWORD=
```

> El host de `DB_URL` debe ser `localhost` al ejecutar fuera de Docker. Nombres como `mysql` o `modulo2-mysql` solo se resuelven dentro de la red de Docker y provocan `UnknownHostException`.

`application.properties` importa el `.env` automáticamente y lee esas variables:

```properties
spring.config.import=optional:file:.env[.properties]
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
```

### Inicialización del esquema

El esquema **no lo genera Hibernate** (`ddl-auto=none`). Lo crea `src/main/resources/schema.sql`, que Spring ejecuta en **cada arranque** (`spring.sql.init.mode=always`). El script es **idempotente**, así que los datos se conservan entre reinicios:

1. Crea cada tabla solo si no existe (`CREATE TABLE IF NOT EXISTS`).
2. Inserta los tres usuarios de ejemplo con `INSERT IGNORE`, sin duplicarlos.

> Si necesitas empezar de cero, elimina las tablas manualmente (o borra el volumen `mysql_data` con `docker compose down -v`) y reinicia la aplicación.

## Puesta en marcha

### Opción A – Docker Compose

```bash
git clone https://github.com/Urian1983/Modulo2JosepEzequiel.git
cd Modulo2JosepEzequiel
docker compose up --build
```

Esto levanta dos contenedores:

| Servicio | Contenedor | Puerto (host → contenedor) | Descripción |
|---|---|---|---|
| `mysql` | `modulo2-mysql` | `3307 → 3306` | MySQL 8.0 con la base `modulo2` y volumen persistente `mysql_data` |
| `app` | `modulo2-app` | `8080 → 8080` | La API; se conecta al servicio `mysql` por la red interna de Docker |

Cuando veas `Started Modulo2InsertaApplication` en el log, la API está lista en `http://localhost:8080/api`.

Notas:

- La app espera a que MySQL esté listo antes de arrancar (`healthcheck` + `depends_on`).
- MySQL se inicia con el usuario `root` **sin contraseña** (`MYSQL_ALLOW_EMPTY_PASSWORD`). Es una configuración pensada solo para desarrollo.
- Para acceder al MySQL del contenedor desde fuera (por ejemplo, con Workbench) usa `localhost:3307`. Se publica en el 3307 para no chocar con un MySQL local en el 3306.
- Los datos se conservan al reiniciar. Para empezar de cero: `docker compose down -v`.
- El `Dockerfile` compila con `-DskipTests`, porque el test `contextLoads` requiere una base de datos accesible.
- La imagen final se ejecuta con un usuario sin privilegios (`spring`).

### Imagen en Docker Hub

La imagen de la aplicación está publicada en Docker Hub: [`urian1983/modulo2-app`](https://hub.docker.com/r/urian1983/modulo2-app).

```bash
docker pull urian1983/modulo2-app:latest
```

La imagen contiene **solo la API**. Necesita un MySQL accesible, por lo que la forma de ejecutarla sigue siendo `docker-compose.yaml`. Para usar la imagen publicada en lugar de compilarla en local, añade la línea `image` al servicio `app`:

```yaml
  app:
    image: urian1983/modulo2-app:latest
    build: .
```

Con eso, `docker compose pull` y `docker compose up` descargan la imagen sin necesidad de compilar el proyecto.

### Opción B – Ejecución local

```bash
git clone https://github.com/Urian1983/Modulo2JosepEzequiel.git
cd Modulo2JosepEzequiel

# Crear la base de datos vacía (las tablas las crea schema.sql al arrancar)
mysql -u root -p -e "CREATE DATABASE modulo2 CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;"

cp .env.example .env     # y editar credenciales

./mvnw spring-boot:run   # En Windows: mvnw.cmd spring-boot:run
```

La API quedará disponible en `http://localhost:8080/api`.

## Endpoints

Todas las rutas cuelgan del prefijo `/api` y requieren autenticación HTTP Basic (ver [roles](#seguridad-y-roles)).

### Profesores

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/api/profesores` | Lista todos los profesores | `200` |
| `GET` | `/api/profesores/{profesorId}` | Obtiene un profesor por id | `200` / `404` |
| `GET` | `/api/profesores/{id}/materias` | Materias que imparte un profesor | `200` |
| `POST` | `/api/profesores` | Crea un profesor | `201` |
| `PUT` | `/api/profesores` | Actualiza un profesor (el `id` va en el cuerpo) | `200` / `404` |
| `DELETE` | `/api/profesores/{id}` | Elimina un profesor | `204` / `404` |

**Cuerpo de ejemplo (`POST` / `PUT`):**

```json
{
  "nombre": "Ana",
  "apellidos": "García",
  "direccion": "Calle Mayor 1"
}
```

### Materias

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/api/materias` | Lista todas las materias | `200` |
| `GET` | `/api/materias/{id}` | Obtiene una materia por id | `200` / `404` |
| `GET` | `/api/materias/{id}/profesor` | Profesor asignado a la materia | `200` / `404` |
| `POST` | `/api/materias` | Crea una materia | `201` |
| `PUT` | `/api/materias` | Actualiza una materia (el `id` va en el cuerpo) | `200` / `404` |
| `DELETE` | `/api/materias/{id}` | Elimina una materia | `204` / `404` |

**Cuerpo de ejemplo (`POST` / `PUT`):**

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
# Listar materias (cualquier rol)
curl -u alumno:1234 http://localhost:8080/api/materias

# Crear un profesor (solo RECTOR)
curl -u rector:1234 -X POST http://localhost:8080/api/profesores \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana","apellidos":"García","direccion":"Av. Diagonal 100"}'

# Actualizar una materia (RECTOR o PROFESOR)
curl -u profesor:1234 -X PUT http://localhost:8080/api/materias \
  -H "Content-Type: application/json" \
  -d '{"id":1,"nombre":"Álgebra","numCreditos":6,"horasCredito":90}'

# Profesor de una materia
curl -u alumno:1234 http://localhost:8080/api/materias/1/profesor

# Eliminar una materia (solo RECTOR)
curl -u rector:1234 -X DELETE http://localhost:8080/api/materias/1
```

### Uso con Postman

Postman permite insertar y consultar datos sin escribir comandos. Con la aplicación arrancada (`http://localhost:8080`):

#### 1. Configurar la autenticación

1. Crea una nueva petición (**New → HTTP Request**).
2. Abre la pestaña **Authorization**.
3. En **Auth Type** elige **Basic Auth**.
4. Introduce usuario y contraseña, por ejemplo `rector` / `1234`.

Postman enviará la cabecera `Authorization` automáticamente. Para no repetirlo en cada petición, puedes definir la autenticación en una **Collection** (pestaña *Authorization* de la colección) y dejar que las peticiones la hereden con **Inherit auth from parent**.

> Para insertar y borrar datos necesitas el usuario `rector`. Con `profesor` solo podrás actualizar (`PUT`) y con `alumno` solo consultar (`GET`).

#### 2. Insertar un profesor (`POST`)

1. Método **POST**, URL `http://localhost:8080/api/profesores`.
2. Pestaña **Body** → marca **raw** → en el desplegable de la derecha elige **JSON**.
3. Escribe el cuerpo:

```json
{
  "nombre": "Ana",
  "apellidos": "García",
  "direccion": "Calle Mayor 1"
}
```

4. Pulsa **Send**. Debes recibir `201 Created` y, en el cuerpo de la respuesta, el profesor con el `id` que le ha asignado MySQL. Apunta ese `id`.

#### 3. Insertar una materia (`POST`)

1. Método **POST**, URL `http://localhost:8080/api/materias`.
2. **Body** → **raw** → **JSON**:

```json
{
  "nombre": "Bases de Datos",
  "numCreditos": 4,
  "horasCredito": 60
}
```

3. **Send**. Respuesta `201 Created` sin cuerpo.

Para crearla ya asignada a un profesor, añade su `id` (el del paso anterior):

```json
{
  "nombre": "Bases de Datos",
  "numCreditos": 4,
  "horasCredito": 60,
  "profesor": { "id": 1 }
}
```

> El profesor debe existir antes de asignarlo. Si el `id` no existe, MySQL rechazará la inserción por la clave foránea.

#### 4. Comprobar que se ha guardado (`GET`)

| Petición | URL |
|---|---|
| Todos los profesores | `GET http://localhost:8080/api/profesores` |
| Todas las materias | `GET http://localhost:8080/api/materias` |
| Materias de un profesor | `GET http://localhost:8080/api/profesores/1/materias` |
| Profesor de una materia | `GET http://localhost:8080/api/materias/1/profesor` |

#### 5. Modificar o borrar

- **Modificar (`PUT`)**: `PUT http://localhost:8080/api/profesores` o `/api/materias`, con el mismo cuerpo JSON **incluyendo el `id`** del registro a actualizar:

```json
{
  "id": 1,
  "nombre": "Ana",
  "apellidos": "García López",
  "direccion": "Calle Mayor 1"
}
```

- **Borrar (`DELETE`)**: `DELETE http://localhost:8080/api/materias/1` (o `/api/profesores/1`). Respuesta `204 No Content` sin cuerpo; si el `id` no existe, `404 Not Found`.

#### Errores habituales en Postman

| Código | Causa probable |
|---|---|
| `401 Unauthorized` | Falta la autenticación Basic o usuario/contraseña incorrectos |
| `403 Forbidden` | El usuario no tiene el rol necesario (por ejemplo, `alumno` intentando un `POST`) |
| `400 Bad Request` | JSON mal formado, o el Body no está en modo **raw → JSON** |
| `415 Unsupported Media Type` | Falta la cabecera `Content-Type: application/json` (se añade sola al elegir **JSON** en el Body) |
| `404 Not Found` | El `id` no existe |

Una petición sin credenciales devuelve `401 Unauthorized`; con credenciales válidas pero sin permiso suficiente, `403 Forbidden`.

## Tests

```bash
./mvnw test
```

El test `contextLoads` arranca el contexto completo de Spring, por lo que necesita MySQL en marcha y el archivo `.env` configurado.

## Limitaciones conocidas

- **Contraseñas en texto plano** (`{noop}`) en los usuarios de ejemplo.
- **Sin datos de ejemplo** de profesores y materias en `schema.sql`; hay que crearlos vía API (ver [Uso con Postman](#uso-con-postman)).