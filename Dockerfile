# Utilizar una imagen base con Java 17
FROM openjdk:17.0-jdk

# Argumentos para el puerto y el JAR
ARG JAR_FILE=target/*.jar
ARG PORT=8080

# Copiar el JAR a la imagen
COPY ${JAR_FILE} app.jar

# Exponer el puerto del contenedor
EXPOSE ${PORT}

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]
