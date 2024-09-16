FROM eclipse-temurin:22-jdk-alpine
VOLUME /tmp
RUN mvn clean package
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
