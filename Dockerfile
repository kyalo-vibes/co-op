# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the target folder to the container
COPY target/bank-simple-0.0.1-SNAPSHOT.jar /app/bank-simple.jar

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/bank-simple.jar"]
