# Base image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY target/diamonds-are-forever-1.0-SNAPSHOT.jar /app/diamonds-are-forever-1.0-SNAPSHOT.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "diamonds-are-forever-1.0-SNAPSHOT.jar"]

