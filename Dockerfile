FROM docker

RUN apk add --no-cache openjdk11

WORKDIR /app

COPY target/app.jar app.jar

CMD ["java", "-jar", "app.jar"]
