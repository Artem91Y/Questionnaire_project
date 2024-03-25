package com.example.demo.controllers;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Question;
import com.example.demo.services.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionRequest questionRequest){
        return questionService.addQuestion(questionRequest);
    }

    @PutMapping("/updateQuestion/{id}")
    public ResponseEntity<String> updateQuestion(@RequestBody QuestionRequest questionRequest, @PathVariable Long id){
        return questionService.updateQuestion(questionRequest, id);
    }

    @GetMapping("/getQuestion/{id}")
    public Question getQuestion(@PathVariable Long id){
        return questionService.getQuestion(id);
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public void deleteQuestion(@PathVariable Long id){
        questionService.deleteQuestion(id);
    }
}
