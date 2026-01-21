# STAGE 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q clean package -DskipTests

# STAGE 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

# Copy whatever jar Maven produced and rename it to app.jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xms256m", "-Xmx400m", "-jar", "app.jar"]
