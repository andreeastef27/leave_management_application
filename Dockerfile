FROM eclipse-temurin:17.0.7_7-jre-alpine

COPY build/libs/java_internship-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080/tcp 8080
EXPOSE 8055/tcp 8055
