# Use an official Maven image to build the application
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as the base image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]