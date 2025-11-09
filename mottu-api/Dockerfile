# Use a lightweight JDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working dir
WORKDIR /app

# Copy the fat JAR produced by Maven build
ARG JAR_FILE=target/mottu-api-1.0.0.jar
COPY ${JAR_FILE} app.jar

# Expose default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]

