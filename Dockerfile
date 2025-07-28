FROM openjdk:8-jdk-alpine
VOLUME /app
ARG DEPENDENCY=target/dependency

RUN mkdir -p /app/uploads
RUN mkdir -p /app/uploads/DESARROLLO
RUN mkdir -p /app/uploads/PRODUCCION

# COPY uploads/*.crt /uploads/
COPY uploads/DESARROLLO/*.crt /uploads/DESARROLLO/
COPY uploads/PRODUCCION/*.crt /uploads/PRODUCCION/

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","sv.mh.fe.Application"]