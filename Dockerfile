FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/spring-boot-new-images.jar spring-boot-new-images.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-new-images.jar"]
