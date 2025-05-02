
# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download dependencies (this layer is cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application JAR, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Create the final runtime image
FROM eclipse-temurin:24-jdk
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/appointment-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]