FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/compression-service-*.jar app.jar

RUN mkdir -p /app/compressed_files

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]