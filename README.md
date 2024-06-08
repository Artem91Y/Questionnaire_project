# Questionnaire service
Our project is made for creating questionnaires in connection with certain or anonymous person. This is realized by using Questionnaire, Question and Person.

# Technologies used
- Java
- Spring Framework
- Spring Boot
- Spring Data JPA
- Spring Security
- Lombok
- Swagger
- Mockito
- Junit
- Mysql
- Maven


# Using API
1) /passQuestionnaire - allows user to pass questionnaire
2) /getActiveQuestionnaire - returns all active questionnaires
3) /deleteQuestionsAnswer - user can cancel his answer
4) /getPassedQuestionnairesWithDetails - user gets all their answers to all answered questions in every questionnaire


# Configuration
1) spring: datasource: url: jdbc:mysql://localhost:{your port}/{your url to DB} - connection to database
2) jpa: hibernate: ddl-auto: {update/create} - your type of adding information to DB

# Data Base
API uses MySQL database

# Contributors
 - [Artem91Y](https://github.com/Artem91Y)
 - [Zhmurenko Viktor](https://github.com/YTypucT)
