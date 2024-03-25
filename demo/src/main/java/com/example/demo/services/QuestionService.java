package com.example.demo.services;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Answer;
import com.example.demo.models.Question;
import com.example.demo.repos.AnswerRepository;
import com.example.demo.repos.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final CheckPermissions checkPermission;

    private final AnswerRepository answerRepository;

    public QuestionService(QuestionRepository questionRepository
            , CheckPermissions checkPermission, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.checkPermission = checkPermission;
        this.answerRepository = answerRepository;
    }


    public ResponseEntity<String> addQuestion(QuestionRequest questionRequest){
        if (questionRequest.getTitle() == null
            || questionRequest.getQuestionnaire() == null
            || questionRequest.getType() == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Question isn't full to be created");
        }
        Question question = new Question();
        question.setQuestionnaire(questionRequest.getQuestionnaire());
        question.setTitle(questionRequest.getTitle());
        question.setType(questionRequest.getType());
        try {
            questionRepository.save(question);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Question is created successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Question isn't saved");
        }
    }

    public ResponseEntity<String> updateQuestion(QuestionRequest questionRequest, Long id){
        Optional<Question> newQuestion = questionRepository.findById(id);
        if (newQuestion.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Question isn't found");
        }
        Question question = new Question();
        if (questionRequest.getTitle() != null) {
            question.setQuestionnaire(questionRequest
                    .getQuestionnaire());
        }
        if (questionRequest.getTitle() != null) {
            question.setTitle(questionRequest.getTitle());
        }
        if (questionRequest.getType() != null){
            question.setType(questionRequest.getType());
        }
        question.setId(id);
        try {
            questionRepository.deleteById(id);
            questionRepository.save(question);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Question is updated successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Question isn't saved");
        }
    }

    public ResponseEntity<String> deleteQuestionsAnswer(Long id, Long answerId){
        Optional<Answer> answerFromDB =  answerRepository.findById(answerId);
        if (answerFromDB.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer isn't found");
        }
        Answer answer = answerFromDB.get();
        if (!checkPermission.hasPermission(answer.getPerson().getId())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("You haven't permission to delete this answer");
        }
        Optional<Question> questionFromDB = questionRepository.findById(id);
        if (questionFromDB.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question isn't found");
        }
        if (!questionFromDB.get().getAnswers().contains(answer)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Question doesn't contain this answer");
        }
        List<Answer> currentAnswers = questionFromDB.get().getAnswers();
        currentAnswers.remove(answer);
        questionFromDB.get().setAnswers(currentAnswers);
        try {
            questionRepository.save(questionFromDB.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Answer is deleted successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Answer isn't deleted");
        }
    }

    public void deleteQuestion(Long id){
        questionRepository.deleteById(id);
    }

    public Question getQuestion(Long id){
        return questionRepository.findById(id).get();
    }
}
