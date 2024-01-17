FROM eclipse-temurin:17.0.2_8-jdk

WORKDIR /app

COPY ./mvnw .
COPY ./.mvn ./.mvn

COPY ./pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./target/web-app-bancario-0.0.1-SNAPSHOT.jar"]

