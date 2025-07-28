FROM openjdk:8-jdk-alpine

WORKDIR /app

# Copiar el .jar con su nombre real
COPY target/sv.mh.fe.api.firmador.jar sv.mh.fe.api.firmador.jar

EXPOSE 8080

# Ejecutar el .jar directamente
ENTRYPOINT ["java", "-jar", "sv.mh.fe.api.firmador.jar"]
