package com.example.demo.controllers;

import com.example.demo.dto.PersonRequest;
import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Person;
import com.example.demo.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Person> deletePerson(@PathVariable Long id) {
        return personService.deletePerson(id);
    }

    @PutMapping("/passQuestionnaire/{questionnaireId}")
    public ResponseEntity<String> passQuestionnaire(@PathVariable Long questionnaireId, @RequestBody List<String> answers) {
        return personService.passQuestionnaire(questionnaireId, answers);
    }

    @GetMapping("/getPassedQuestionnairesWithDetails")
    public Map<String, Map<QuestionRequest, String>> getPassedQuestionnairesWithDetails(){
        return personService.getPassedQuestionnairesWithDetails();
    }

}
