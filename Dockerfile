FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

RUN mkdir /app/uploads

RUN addgroup -S app && adduser -S app -G app

USER app

COPY --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
