FROM openjdk:17-jdk-alpine as builder

WORKDIR /app

COPY ./mvnw .
COPY ./.mvn ./.mvn

COPY ./pom.xml .


RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app
RUN mkdir ./logs
ARG TARGET_FOLDER=/app/target
COPY --from=builder ${TARGET_FOLDER}/web-app-bancario-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=8080
ENV PORT ${PORT_APP}
EXPOSE $PORT

CMD sleep 20 && java -jar web-app-bancario-0.0.1-SNAPSHOT.jar
#CMD ["java", "-jar", "web-app-bancario-0.0.1-SNAPSHOT.jar"]

