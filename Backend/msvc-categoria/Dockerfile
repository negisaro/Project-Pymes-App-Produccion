FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copia el jar de la aplicación
COPY target/msvc-categoria-0.0.1-SNAPSHOT.jar app.jar

# Copia el script wait-for-it.sh al contenedor
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

EXPOSE 8083

# Espera a que los servicios MySQL y usuario estén listos antes de ejecutar la app
ENTRYPOINT ["/bin/bash", "-c", "/app/wait-for-it.sh db:3306 --timeout=600 --strict -- /app/wait-for-it.sh usuario:8081 --timeout=600 --strict -- java -jar app.jar"]