# ---------- Etapa 1: compilar el proyecto ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Primero solo lo necesario para descargar dependencias.
# Docker cachea este paso mientras el pom.xml no cambie.
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Después el código fuente y la compilación.
# -DskipTests porque el test contextLoads necesita una base de datos.
COPY src src
RUN ./mvnw -B package -DskipTests

# ---------- Etapa 2: imagen final, solo con lo necesario para ejecutar ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build /app/target/*.jar app.jar

USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]