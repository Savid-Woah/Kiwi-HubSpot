FROM maven:3.9.6 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:resolve

COPY . .

RUN mvn clean

RUN mvn package -DskipTests

FROM openjdk:21-jdk-slim AS final

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY --from=build /app/src/main/resources/ /app/resources/

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]