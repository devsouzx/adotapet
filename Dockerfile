FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B package

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /app/target/adotapet-0.0.1-SNAPSHOT.jar app.jar
ENV SERVER_PORT=10000
EXPOSE 10000
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
