package com.example.demo.services;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Question;
import com.example.demo.repos.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public ResponseEntity<String> addQuestion(QuestionRequest questionRequest){
        if (questionRequest.getName() == null
            || questionRequest.getQuestionnaire() == null
            || questionRequest.getType() == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Question isn't full to be created");
        }
        Question question = new Question();
        question.setQuestionnaire(questionRequest.getQuestionnaire());
        question.setName(questionRequest.getName());
        question.setType(questionRequest.getType());
        try {
            questionRepository.save(question);
            return ResponseEntity.status(HttpStatus.CREATED).body("Question is created successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Question isn't saved");
        }
    }

    public ResponseEntity<String> updateQuestion(QuestionRequest questionRequest, Long id){
        Optional<Question> newQuestion = questionRepository.findById(id);
        if (newQuestion.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question isn't found");
        }
        Question question = new Question();
        if (questionRequest.getName() != null) {
            question.setQuestionnaire(questionRequest.getQuestionnaire());
        }
        if (questionRequest.getName() != null) {
            question.setName(questionRequest.getName());
        }
        if (questionRequest.getType() != null){
            question.setType(questionRequest.getType());
        }
        question.setId(id);
        try {
            questionRepository.deleteById(id);
            questionRepository.save(question);
            return ResponseEntity.status(HttpStatus.CREATED).body("Question is updated successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Question isn't saved");
        }
    }
}
