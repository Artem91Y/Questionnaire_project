package com.example.demo.controllers;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Question;
import com.example.demo.services.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionRequest questionRequest) {
        return questionService.addQuestion(questionRequest);
    }

    @PutMapping("/updateQuestion/{id}")
    public ResponseEntity<String> updateQuestion(@RequestParam String title, @RequestBody QuestionRequest questionRequest) {
        return questionService.updateQuestion(questionRequest, title);
    }

    @GetMapping("/getQuestion/{id}")
    public ResponseEntity<Question> getQuestion(@RequestParam String title) {
        return questionService.getQuestion(title);
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public ResponseEntity<Question> deleteQuestion(@RequestParam String title) {
        return questionService.deleteQuestion(title);
    }

    @DeleteMapping("/deleteAnswer/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable Long answerId, @RequestParam String title) {
        return questionService.deleteQuestionsAnswer(title, answerId);
    }
}
