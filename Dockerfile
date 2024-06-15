FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY target/Questionnaire_project-0.0.1-SNAPSHOT.jar /app/Questionnaire_project.jar
ENTRYPOINT ["java", "-jar", "Questionnaire_project.jar"]
EXPOSE 8080
