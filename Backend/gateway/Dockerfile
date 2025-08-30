# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-jammy
RUN useradd -m gatewayuser
WORKDIR /app

COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx1024m"
ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-dev}

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

USER gatewayuser

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar --server.port=$SERVER_PORT --spring.profiles.active=$SPRING_PROFILES_ACTIVE"]