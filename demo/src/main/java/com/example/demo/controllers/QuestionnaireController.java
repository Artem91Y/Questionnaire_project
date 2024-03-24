package com.example.demo.controllers;

import com.example.demo.models.Questionnaire;
import com.example.demo.services.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PostMapping("/addQuestionnaire")
    public ResponseEntity<String> addQuestionnaire(@RequestParam String name
            , @RequestParam String description
            , @RequestParam LocalDate startTime
            , @RequestParam LocalDate endTime){
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(name);
        questionnaire.setStartTime(startTime);
        questionnaire.setEndTime(endTime);
        questionnaire.setDescription(description);
        return questionnaireService.addQuestionnaire(questionnaire);
    }

    @PutMapping("/updateQuestionnaire")
    public ResponseEntity<String> updateQuestionnaire(
            @RequestParam String name
            ,@RequestParam Long id
            , @RequestParam String description
            , @RequestParam LocalDate startTime
            , @RequestParam LocalDate endTime){
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(name);
        questionnaire.setStartTime(startTime);
        questionnaire.setEndTime(endTime);
        questionnaire.setDescription(description);
        return questionnaireService.updateQuestionnaire(questionnaire, id);
    }

    @DeleteMapping("/deleteQuestionnaire")
    public void deleteQuestionnaire(Long id){
        questionnaireService.deleteQuestionnaire(id);
    }


}
