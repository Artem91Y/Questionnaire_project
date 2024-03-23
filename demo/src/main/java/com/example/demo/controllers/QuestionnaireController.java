package com.example.demo.controllers;

import com.example.demo.models.Questionnaire;
import com.example.demo.services.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/updateQuestionnaire")
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

    @GetMapping("/getQuestionnaire")
    public Questionnaire getQuestionnaire(@RequestParam Long id){
        return questionnaireService.getQuestionnaire(id);
    }

    @DeleteMapping("/deleteQuestionnaire")
    public void deleteQuestionnaire(Long id){
        questionnaireService.deleteQuestionnaire(id);
    }


}
