FROM openjdk:12
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Autenticacion.jar
#ENTRYPOINT ["java","-jar","/app.jar", "--spring.config.location=file:/application.properties"]
ENTRYPOINT ["java","-jar","/Autenticacion.jar"]