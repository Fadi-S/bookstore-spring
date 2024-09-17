# Use an Alpine-based JDK image
FROM eclipse-temurin:22-jdk-alpine

# Install Maven in the Alpine image
RUN apk add --no-cache maven

# Create a directory for the application
WORKDIR /app

# Copy the Maven project into the container
COPY . .

# Build the Maven project
RUN mvn clean package -DskipTests

# Copy the built jar file to a dedicated location
COPY target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
