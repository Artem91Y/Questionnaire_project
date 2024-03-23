package com.example.demo.services;

import com.example.demo.dto.PersonRequest;
import com.example.demo.models.Person;
import com.example.demo.models.Question;
import com.example.demo.models.Questionnaire;
import com.example.demo.repos.PersonRepository;
import com.example.demo.repos.QuestionRepository;
import com.example.demo.repos.QuestionnaireRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final QuestionnaireRepository questionnaireRepository;


    public PersonService(PersonRepository personRepository, QuestionnaireRepository questionnaireRepository) {
        this.personRepository = personRepository;
        this.questionnaireRepository = questionnaireRepository;
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

    public Person getPerson(long personId) {
        return personRepository.findById(personId).get();
    }

    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }

    public ResponseEntity<String> passQuestionnaire(String fullName, Long questionnaireId, List<String> answers){
        Optional<Questionnaire> questionnaireForCheck = questionnaireRepository.findById(questionnaireId);
        if (questionnaireForCheck.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionnaire isn't found");
        }
        for (Question question : questionnaireForCheck.get().getQuestions()) {


        }
    }




}
