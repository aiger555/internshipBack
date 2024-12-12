## Use the official OpenJDK base image
#FROM openjdk:17-jdk-slim
#
## Set the working directory in the container
#WORKDIR /app
#
## Copy the jar file from the target directory of your project
#COPY target/demo.jar /app/demo.jar
#
## Expose the port your Spring Boot application will run on
#EXPOSE 8090
#
## Run the jar file when the container starts
#ENTRYPOINT ["java", "-jar", "/app/demo.jar"]
