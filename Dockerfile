FROM openjdk:21-jdk-slim
COPY target/assignment-sales-service-1.0.0.jar app.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080","-jar","app.jar"]