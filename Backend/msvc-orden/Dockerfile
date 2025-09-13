FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copia el jar de la aplicación
COPY target/msvc-orden-0.0.1-SNAPSHOT.jar app.jar

# Copia el script wait-for-it.sh al contenedor
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

EXPOSE 8086

# Espera a que el servicio msvc-producto esté listo antes de ejecutar la app
ENTRYPOINT ["/bin/bash", "-c", "/app/wait-for-it.sh msvc-producto:8085 --timeout=600 --strict -- java -jar app.jar"]