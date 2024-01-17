FROM eclipse-temurin:17.0.2_8-jdk

WORKDIR /app

COPY ./target/web-app-bancario-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT  ["java", "-jar", "web-app-bancario-0.0.1-SNAPSHOT.jar"]

