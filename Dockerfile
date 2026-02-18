FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Keep backend small: heap + metaspace capped
ENV JAVA_TOOL_OPTIONS="-Xms128m -Xmx320m -XX:MaxMetaspaceSize=128m -XX:+UseSerialGC -Dspring.jmx.enabled=false -Dspring.main.banner-mode=off"

ENTRYPOINT ["java", "-jar", "app.jar"]
