FROM amazoncorretto:17
EXPOSE 8080
COPY target/meal2-0.0.1-SNAPSHOT.jar meal2-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/meal2-0.0.1-SNAPSHOT.jar"]
