FROM eclipse-temurin:21-jre-alpine
VOLUME /temp
EXPOSE 8083
ARG JAR_FILE=applications/app-service/build/libs/CrediYa.jar
COPY ${JAR_FILE} /autenticacion.jar
ENTRYPOINT  ["java", "-jar", "/autenticacion.jar"]