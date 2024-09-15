FROM eclipse-temurin:22-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
RUN mvn clean package
ENTRYPOINT ["java","-jar","/app.jar"]
