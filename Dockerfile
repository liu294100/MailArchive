FROM openjdk:8-jdk-alpine

# Add maintainer info
LABEL maintainer="apollo@example.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Set application's JAR file
ARG JAR_FILE=target/mail-archive-1.0-SNAPSHOT.jar

# Add the application's JAR file to the container
COPY ${JAR_FILE} app.jar

# Create attachments directory
RUN mkdir -p /data/attachments

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"] 