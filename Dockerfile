FROM adoptopenjdk/openjdk8
ADD target/demo-0.0.1-SNAPSHOT.jar /demo.jar
EXPOSE 28084/tcp
ENTRYPOINT ["java", "-jar", "/demo.jar"]