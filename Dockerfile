# ---- Stage 1: Build the application ----
FROM maven:3.9.9-eclipse-temurin-24 AS build
WORKDIR /app
# Copy Maven project files
# Package the application (skip tests optionally)
#FROM eclipse-temurin:24-jre
#WORKDIR /app
# Copy built JAR from previous stage
#COPY --from=build target/*.jar app.jar

# Run the Spring Boot application
#ENTRYPOINT ["java", "-jar", "app.jar"]

