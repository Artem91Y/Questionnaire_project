package com.example.demo.services;

import com.example.demo.dto.PersonRequest;
import com.example.demo.models.Answer;
import com.example.demo.models.Person;
import com.example.demo.models.Question;
import com.example.demo.models.Questionnaire;
import com.example.demo.repos.PersonRepository;
import com.example.demo.repos.QuestionnaireRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final QuestionnaireRepository questionnaireRepository;

    private final CheckPermissions checkPermission;


    public PersonService(PersonRepository personRepository, QuestionnaireRepository questionnaireRepository, CheckPermissions checkPermission) {
        this.personRepository = personRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.checkPermission = checkPermission;
    }

    public ResponseEntity<String> savePerson(PersonRequest personForCreating) {
        if (personForCreating.getEmail() == null
                || personForCreating.getFullName() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Person isn't full to be saved");
        }

        Person createdPerson = new Person();
        createdPerson.setEmail(personForCreating.getEmail());
        createdPerson.setFullName(personForCreating.getFullName());
        createdPerson.setUsername(personForCreating.getUsername());

        try {
            personRepository.save(createdPerson);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Person is created successfully");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Person not saved");
        }
    }

    public ResponseEntity<String> updatePerson(long id
            , PersonRequest personForCreating) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Person %s number not found", id));
        }

        Person createdPerson = person.get();
        if (personForCreating.getFullName() != null) {
            createdPerson.setFullName(personForCreating.getFullName());
        }
        if (personForCreating.getEmail() != null) {
            createdPerson.setEmail(personForCreating.getEmail());
        }

        try {
            personRepository.save(createdPerson);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Person updated successfully");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Person not updated");
        }
    }

    public void deletePerson(long personId) {
        personRepository.deleteById(personId);
    }

    public ResponseEntity<String> passQuestionnaire(String username, Long questionnaireId, List<String> answers){
        Optional<Questionnaire> questionnaireForCheck = questionnaireRepository
                .findById(questionnaireId);
        if (questionnaireForCheck.isEmpty()){
            return ResponseEntity.status(HttpStatus
                    .NOT_FOUND).body("Questionnaire isn't found");
        }
        Questionnaire questionnaire = questionnaireForCheck.get();

        Optional<Person> client = personRepository
                .findByUsername(username);
        if (client.isEmpty()){
            return ResponseEntity.status(HttpStatus
                    .NOT_FOUND).body("Person isn't found");
        }
        Person answeringPerson = client.get();

        if (!checkPermission.hasPermission(answeringPerson.getId())){
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("You haven't permission for this service");
        }

        List<Question> questions = questionnaireForCheck
                .get().getQuestions();
        if (questions.size() < answers.size()){
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("You haven't answered all the question");
        } else if (questions.size() > answers.size()){
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("You gave too many answers");
        }

        List<Question> answeredQuestions = new ArrayList<>();
        for (int i = 0; i < questions.size(); i ++) {
            Question question = questions.get(i);
            Answer answer = new Answer();
            answer.setPerson(answeringPerson);
            answer.setAnswerText(answers.get(i));

            List<Answer> answersForAdding = question.getAnswers();
            answersForAdding.add(answer);
            question.setAnswers(answersForAdding);
            answeredQuestions.add(question);
            List<Question> savedQuestions = answeringPerson.getQuestionsPassed();
            savedQuestions.add(question);
            answeringPerson.setQuestionsPassed(savedQuestions);
        }
        questionnaire.setQuestions(answeredQuestions);
        try {
            personRepository.save(answeringPerson);
            questionnaireRepository.save(questionnaire);
            return ResponseEntity.ok("Questionnaire passed successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("Questionnaire isn't passed");
        }
    }




}
