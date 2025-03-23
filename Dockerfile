# Use the official OpenJDK 17 JRE as the base image
FROM openjdk:17-jdk
WORKDIR /demo
COPY target/demo.jar demo.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "demo.jar"]