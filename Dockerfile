# FROM openjdk:8-jdk-alpine

# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera con JDK Alpine
FROM eclipse-temurin:17-jdk-alpine

ENV TZ="America/El_Salvador"
WORKDIR /app

# Instalar dependencias necesarias
RUN apk add --no-cache \
    tzdata \
    dbus \
    glib-networking \
    dconf \
    fontconfig && \
    addgroup -S spring && adduser -S spring -G spring

# Copiar el jar generado en build (renombrado para simplificar)
COPY --from=build /app/target/securecomerce-firmador.jar /app/securecomerce-firmador.jar

# Si tienes reports, los copias también (opcional)
COPY reports /app/reports

RUN chown -R spring:spring /app
USER spring

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport \
                       -XX:+UseG1GC \
                       -XX:MaxGCPauseMillis=100 \
                       -XX:InitiatingHeapOccupancyPercent=35 \
                       -XX:+UseStringDeduplication"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "securecomerce-firmador.jar"]

