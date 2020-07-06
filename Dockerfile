FROM maven:3.6.3-jdk-8 AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY web-api/pom.xml web-api/pom.xml
COPY services/pom.xml services/pom.xml
COPY persistence/pom.xml persistence/pom.xml

COPY web-api/src /workspace/web-api/src
COPY services/src /workspace/services/src
COPY persistence/src /workspace/persistence/src
RUN mvn -f pom.xml clean package

FROM openjdk:8-alpine
COPY --from=build /workspace/web-api/target/cv-exec.jar cv.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","cv.jar"]
