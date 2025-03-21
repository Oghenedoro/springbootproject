# Use the official OpenJDK 17 JRE as the base image
FROM openjdk:17-jdk
WORKDIR /demo
COPY target/demo.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]