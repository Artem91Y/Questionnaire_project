package com.example.demo.controllers;

import com.example.demo.dto.PersonRequest;
import com.example.demo.models.Answer;
import com.example.demo.models.Person;
import com.example.demo.models.Question;
import com.example.demo.models.Questionnaire;
import com.example.demo.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class PersonController {

    private final PersonService personService;


    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/addPerson")
    public ResponseEntity<String> addPerson(@RequestBody PersonRequest personRequest) {
        return personService.savePerson(personRequest);
    }

    @PutMapping("/updatePerson/{id}")
    public ResponseEntity<String> updatePerson(@RequestBody PersonRequest personRequest, @PathVariable Long id) {
        return personService.updatePerson(id, personRequest);
    }

    @DeleteMapping("/deletePerson/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }

    @PutMapping("/passQuestionnaire/{questionnaireId}")
    public ResponseEntity<String> passQuestionnaire(@PathVariable Long questionnaireId, @RequestBody List<String> answers) {
        return personService.passQuestionnaire(questionnaireId, answers);
    }

//    TODO delete controller and service getPersonQuestionnaires
    @GetMapping("/getPersonQuestionnaires/{id}")
    public Set<Questionnaire> getPersonQuestionnaires(@PathVariable Long id){
        return personService.getPersonQuestionnaires(id);
    }

    @GetMapping("/getPassedQuestionnairesWithDetails")
    public Map<String, Map<Question, String>> getPassedQuestionnairesWithDetails(){
        return personService.getPassedQuestionnairesWithDetails();
    }

}
