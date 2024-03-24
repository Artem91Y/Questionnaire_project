package com.example.demo.controllers;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Question;
import com.example.demo.services.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PutMapping("/addQuestion")
    public ResponseEntity<String> updateQuestion(@RequestBody QuestionRequest questionRequest){
        return questionService.addQuestion(questionRequest);
    }

    @GetMapping("/getQuestion")
    public Question getQuestion(Long id){
        return questionService.getQuestion(id);
    }
}
