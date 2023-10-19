# https://to-do-list-xitr.onrender.com

FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17 -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim
EXPOSE 8080

COPY --from=build /target/todolist-1.0.0.jar app.jar
# java -jar .\target\todolist-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app.jar"]