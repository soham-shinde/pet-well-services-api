FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/pet-well-api-0.0.1-SNAPSHOT.jar pet-well-service.jar
EXPOSE 8080 
ENTRYPOINT ["java","-jar","pet-well-service.jar"]