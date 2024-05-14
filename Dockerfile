FROM openjdk:17-alpine

LABEL authors="ahmedukamel"

RUN apk add bash

WORKDIR /app

RUN mkdir -p "/app/images/product" "/app/images/profile"

COPY  target/*.jar application.jar

EXPOSE 8080

CMD ["java", "-jar", "application.jar"]