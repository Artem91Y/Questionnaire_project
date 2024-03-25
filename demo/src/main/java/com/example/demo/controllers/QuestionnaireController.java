package com.example.demo.controllers;

import com.example.demo.dto.QuestionnaireRequest;
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
    public ResponseEntity<String> addQuestionnaire(@RequestParam QuestionnaireRequest questionnaire){
        return questionnaireService.addQuestionnaire(questionnaire);
    }

    @PutMapping("/updateQuestionnaire/{id}")
    public ResponseEntity<String> updateQuestionnaire(@RequestBody QuestionnaireRequest questionnaire,@PathVariable Long id){

        return questionnaireService.updateQuestionnaire(questionnaire, id);
    }

    @DeleteMapping("/deleteQuestionnaire")
    public void deleteQuestionnaire(Long id){
        questionnaireService.deleteQuestionnaire(id);
    }


}
