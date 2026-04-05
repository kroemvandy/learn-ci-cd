FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/spring-boot-example.jar spring-boot-example.jar
EXPOSE 8080
CMD ["java", "-jar", "spring-boot-example.jar"]
