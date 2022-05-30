FROM openjdk:17
VOLUME /tmp
COPY target/*.jar users-system.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/urandom", "-jar", "/users-system.jar"]