# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory inside container
WORKDIR /app

# Copy pom.xml and download dependencies (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire source code and build the app
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the app using a lightweight JDK image
FROM eclipse-temurin:21-jre-alpine

# Create a non-root user for security
RUN adduser -D springuser
USER springuser

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 9001

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
