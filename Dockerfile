FROM maven:3.8.6-openjdk-11 AS BUILD_IMAGE

LABEL stage=aegir-builder

RUN mkdir -p /app

WORKDIR /app

COPY . /app

RUN mvn -pl backend clean verify -P typescript
RUN mvn clean package

FROM adoptopenjdk/openjdk11:alpine

ENV NODE_ENV production

LABEL com.centurylinklabs.watchtower.enable="true"
LABEL com.centurylinklabs.watchtower.stop-signal="SIGHUP"

WORKDIR /app

COPY --from=BUILD_IMAGE /app/backend/target/aegir.jar /app/aegir.jar

EXPOSE 8080

CMD ["/usr/bin/java", "-jar", "/app/aegir.jar"]
