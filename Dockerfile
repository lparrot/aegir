FROM maven:3.8.6-openjdk-8 AS BUILD_IMAGE

LABEL stage=aegir-builder

RUN mkdir -p /app

WORKDIR /app

COPY . /app

RUN mvn -pl backend compile --quiet
RUN mvn clean package --quiet

FROM adoptopenjdk/openjdk8:alpine

ENV NODE_ENV production

LABEL com.centurylinklabs.watchtower.enable="true"

WORKDIR /app

COPY --from=BUILD_IMAGE /app/backend/target/aegir.jar /app/aegir.jar

EXPOSE 8080

CMD ["/usr/bin/java", "-jar", "/app/aegir.jar"]
