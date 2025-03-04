FROM alpine:latest AS build

RUN apk upgrade --no-cache \
    && apk add --no-cache maven openjdk17

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -T 2C -q clean package -DskipTests

FROM alpine:latest

RUN apk upgrade --no-cache \
    && apk add --no-cache curl openjdk17-jre

WORKDIR /app

COPY --from=build /app /app

CMD java -jar target/analysis-manager-0.2.0-SNAPSHOT.jar