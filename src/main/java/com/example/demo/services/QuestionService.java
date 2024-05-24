package com.example.demo.services;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Answer;
import com.example.demo.models.Question;
import com.example.demo.repos.AnswerRepository;
import com.example.demo.repos.QuestionRepository;
import com.example.demo.repos.QuestionnaireRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;


    private final AnswerRepository answerRepository;

    private final QuestionnaireRepository questionnaireRepository;

    public QuestionService(QuestionRepository questionRepository
            , AnswerRepository answerRepository
            , QuestionnaireRepository questionnaireRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionnaireRepository = questionnaireRepository;
    }


    public ResponseEntity<String> addQuestion(QuestionRequest questionRequest) {
        if (questionRequest.getTitle() == null
                || questionRequest.getType() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Question isn't full to be created");
        }
        Question question = new Question();
        question.setQuestionnaire(questionnaireRepository.findByName(questionRequest.getQuestionnaireName()));
        question.setTitle(questionRequest.getTitle());
        question.setType(questionRequest.getType());
        try {
            questionRepository.save(question);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Question is created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Question isn't saved");
        }
    }

    public ResponseEntity<String> updateQuestion(QuestionRequest questionRequest, Long id) {
        Optional<Question> newQuestion = questionRepository.findById(id);
        if (newQuestion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Question isn't found");
        }
        Question question = newQuestion.get();
        if (questionRequest.getTitle() != null) {
            question.setTitle(questionRequest.getTitle());
        }
        if (questionRequest.getType() != null) {
            question.setType(questionRequest.getType());
        }
        try {
            questionRepository.save(question);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Question is updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Question isn't saved");
        }
    }

//    TODO refactor id -> title (in other methods)

    public ResponseEntity<String> deleteQuestionsAnswer(String title, Long answerId) {
        Optional<Answer> answerFromDB = answerRepository.findById(answerId);
        if (answerFromDB.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer isn't found");
        }
        Answer answer = answerFromDB.get();
        Optional<Question> questionFromDB = questionRepository.findQuestionByTitle(title);
        if (questionFromDB.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question isn't found");
        }
        if (questionFromDB.get().getAnswers() == null || !questionFromDB.get().getAnswers().contains(answer)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Question doesn't contain this answer");
        }
        List<Answer> currentAnswers = questionFromDB.get().getAnswers();
        LinkedList<Answer> newCurrentAnswers = new LinkedList<>(currentAnswers);
        newCurrentAnswers.remove(answer);
        currentAnswers = newCurrentAnswers.stream().toList();
        questionFromDB.get().setAnswers(currentAnswers);
        try {
            questionRepository.save(questionFromDB.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Answer is deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Answer isn't deleted");
        }
    }


//    TODO remake delete ResponseEntity<String> -> ResponseEntity<Question>
    public ResponseEntity<String> deleteQuestion(Long id) {
        try {
            if (questionRepository.findById(id).isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question doesn't exist");
            }
            Question question = questionRepository.findById(id).get();
            questionRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(question.toString());

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete question");
        }

    }


    public ResponseEntity<Question> getQuestion(Long id) {
        try {
            if (questionRepository.findById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(questionRepository.findById(id).get());
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
}
