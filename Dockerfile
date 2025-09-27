# Stage 1: Build the JAR
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Kopiert das erstellte JAR-File in das finale Image
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]