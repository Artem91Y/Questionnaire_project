package com.example.demo.controllers;

import com.example.demo.dto.QuestionnaireRequest;
import com.example.demo.models.Questionnaire;
import com.example.demo.services.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PostMapping("/addQuestionnaire")
    public ResponseEntity<String> addQuestionnaire(@RequestBody QuestionnaireRequest questionnaire) {
        return questionnaireService.addQuestionnaire(questionnaire);
    }

    @PutMapping("/updateQuestionnaire/{id}")
    public ResponseEntity<String> updateQuestionnaire(@RequestBody QuestionnaireRequest questionnaire, @RequestParam String name) {
        return questionnaireService.updateQuestionnaire(questionnaire, name);
    }

    @DeleteMapping("/deleteQuestionnaire/{id}")
    public ResponseEntity<Questionnaire> deleteQuestionnaire(@RequestParam String name) {
        return questionnaireService.deleteQuestionnaire(name);
    }

    @GetMapping("/getQuestionnaire/{id}")
    public ResponseEntity<Questionnaire> getQuestionnaire(@RequestParam String name) {
        return questionnaireService.getQuestionnaire(name);
    }
    @GetMapping("/getActiveQuestionnaires")
    public List<Questionnaire> getActiveQuestionnaires(){
        return questionnaireService.getActiveQuestionnaires();
    }

}
