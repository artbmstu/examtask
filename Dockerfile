FROM openjdk:19-jdk-alpine

WORKDIR /app

COPY build/libs/examtask-0.0.1.jar .

EXPOSE 8080

CMD ["java", "-jar", "examtask-0.0.1.jar"]