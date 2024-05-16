# Use a Maven image to build and package the application
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

# Create the final image with the packaged JAR
FROM openjdk:17
WORKDIR /app

# Create the directory
RUN mkdir -p /var/lib/educapp-backend/images

# Set directory permissions
RUN chmod 777 /var/lib/educapp-backend/images

# Copy the packaged JAR
COPY --from=builder /app/target/*.jar app.jar

# Define a volume for the data directory
VOLUME /var/lib/educapp-backend/images

ENTRYPOINT ["java", "-jar", "app.jar"]
