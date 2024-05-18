package com.example.demo.services;

import com.example.demo.dto.PersonRequest;
import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Answer;
import com.example.demo.models.Person;
import com.example.demo.models.Question;
import com.example.demo.models.Questionnaire;
import com.example.demo.repos.AnswerRepository;
import com.example.demo.repos.PersonRepository;
import com.example.demo.repos.QuestionnaireRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final QuestionnaireRepository questionnaireRepository;


    private final AnswerRepository answerRepository;


    public PersonService(PersonRepository personRepository, QuestionnaireRepository questionnaireRepository, AnswerRepository answerRepository) {
        this.personRepository = personRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.answerRepository = answerRepository;
    }

    public ResponseEntity<String> savePerson(PersonRequest personForCreating) {
        if (personForCreating.getFullName() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Person isn't full to be saved");
        }

        Person createdPerson = new Person();
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

    public ResponseEntity<String> passQuestionnaire(Long questionnaireId, List<String> answers) {
        Optional<Questionnaire> questionnaireForCheck = questionnaireRepository
                .findById(questionnaireId);
        if (questionnaireForCheck.isEmpty()) {
            return ResponseEntity.status(HttpStatus
                    .NOT_FOUND).body("Questionnaire isn't found");
        }
        Questionnaire questionnaire = questionnaireForCheck.get();

        Person answeringPerson;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser")) {
            answeringPerson = personRepository.findByUsername("anonymous").get();
        } else {
            Optional<Person> answeringPersonFromDB = personRepository.findByUsername(authentication.getName());
            if (answeringPersonFromDB.isEmpty()) {
                return ResponseEntity.status(HttpStatus
                        .NOT_FOUND).body("Person isn't found");
            }
            answeringPerson = answeringPersonFromDB.get();
        }

        List<Question> questions = questionnaireForCheck
                .get().getQuestions();
        if (questions.size() > answers.size()) {
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("You haven't answered all the question");
        } else if (questions.size() < answers.size()) {
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("You gave too many answers");
        }
        Set<Questionnaire> answeringPersonQuestionnaires = answeringPerson.getQuestionnairesPassed();
        if (answeringPersonQuestionnaires == null){
            answeringPersonQuestionnaires = new HashSet<>();
        }
        if (answeringPersonQuestionnaires.contains(questionnaire)) {
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("You have already passed this questionnaire");
        }
        answeringPersonQuestionnaires.add(questionnaire);
        answeringPerson.setQuestionnairesPassed(answeringPersonQuestionnaires);
        List<Question> answeredQuestions = new ArrayList<>();
        try {
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                Answer answer = new Answer();
                answer.setPerson(answeringPerson);
                answer.setAnswerText(answers.get(i));

                List<Answer> answersForAdding = question.getAnswers();
                if (answersForAdding != null){
                    answersForAdding.add(answer);
                }
                else{
                    answersForAdding = List.of(answer);
                }
                question.setAnswers(answersForAdding);
                answeredQuestions.add(question);
                answerRepository.save(answer);
            }
            questionnaire.setQuestions(answeredQuestions);
            personRepository.save(answeringPerson);
            questionnaireRepository.save(questionnaire);
            return ResponseEntity.ok("Questionnaire passed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body("Questionnaire isn't passed");
        }
    }

    public Set<Questionnaire> getPersonQuestionnaires(Long id) {
        Optional<Person> personFromDB = personRepository.findById(id);
        if (personFromDB.isEmpty()) {
            return null;
        }
        return personFromDB.get().getQuestionnairesPassed();
    }

    public Map<String, Map<QuestionRequest, String>> getPassedQuestionnairesWithDetails() {
        Person person;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser")) {
            return null;
        } else {
            Optional<Person> answeringPersonFromDB = personRepository.findByUsername(authentication.getName());
            if (answeringPersonFromDB.isEmpty()) {
                return null;
            }
            person = answeringPersonFromDB.get();
        }
        Set<Questionnaire> questionnaires = person.getQuestionnairesPassed();
        Map<String, Map<QuestionRequest, String>> result = new HashMap<>();
        for (Questionnaire questionnaire : questionnaires) {
            for (Question question : questionnaire.getQuestions()) {
                for (Answer answer : question.getAnswers()) {
                    if (answer.getPerson().equals(person)) {
                        QuestionRequest questionRequest = new QuestionRequest(question.getTitle(), questionnaire.getName(), question.getType());
                        result.put(questionnaire.getName(), Map.of(questionRequest, answer.getAnswerText()));
                    }
                }
            }
        }
        return result;
    }


}
