FROM openjdk:17-jdk-alpine

# Create user to run app as (instead of root)
RUN addgroup -S app && adduser -S app -G app

# User app
USER app

# Copy the jar file into the container
COPY target/*.jar /app/app.jar

# Set the working directory
WORKDIR /app

# Expose the port
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "/app/app.jar"]