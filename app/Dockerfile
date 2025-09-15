# ===== Etapa de build (con Maven) =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copiamos POM primero para cachear dependencias
COPY pom.xml .
# Descarga dependencias sin compilar tests (rápido y cacheable)
RUN mvn -q -e -DskipTests dependency:go-offline

# Ahora sí copiamos el código y construimos
COPY src ./src
RUN mvn -q -DskipTests clean package

# ===== Etapa de runtime (JRE) =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Usuario no root
RUN useradd -r -u 1001 appuser
USER appuser

# Copia el JAR sombreado si lo generas, o el jar normal si lleva dependencias dentro
# Ajusta el nombre si tu build genera "app-1.0.0.jar" (shade ya lo dejó así en tu caso)
COPY --from=build /workspace/target/app-1.0.0.jar /app/app.jar

# Variables de entorno para DB (se sobreescriben al ejecutar)
ENV DB_HOST=localhost \
    DB_PORT=5432 \
    DB_NAME=monolito_ecommerce \
    DB_USER=postgres \
    DB_PASSWORD=postgres \
    DB_SSLMODE=disable

# Tu app es de consola (no expone HTTP), así que no necesitamos puertos
ENTRYPOINT ["java","-jar","/app/app.jar"]
