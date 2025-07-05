FROM eclipse-temurin:24-alpine as builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:24-alpine

COPY --from=builder /app/target/*.jar /app/application.jar

RUN apk add --no-cache imagemagick

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/application.jar"]