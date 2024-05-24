package com.example.demo.services;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Answer;
import com.example.demo.models.Question;
import com.example.demo.models.Questionnaire;
import com.example.demo.models.enums.TypeOfAnswer;
import com.example.demo.repos.AnswerRepository;
import com.example.demo.repos.QuestionRepository;
import com.example.demo.repos.QuestionnaireRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    public QuestionServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestAddQuestionPositive() {
        when(questionnaireRepository.findByName("mathematics")).thenReturn(
                new Questionnaire(1L, "mathematics", "description", null, null, null));

        ResponseEntity<String> response = questionService.addQuestion(
                new QuestionRequest("cfae", "mathematics", TypeOfAnswer.ONE_ANSWER));
        assertEquals(ResponseEntity.status(HttpStatus.CREATED)
                .body("Question is created successfully"), response);

    }

    @Test
    public void TestAddQuestionNegativeNotFullObject() {
        when(questionnaireRepository.findByName("mathematics")).thenReturn(
                new Questionnaire(1L, "mathematics", "description", null, null, null));

        ResponseEntity<String> response = questionService.addQuestion(
                new QuestionRequest("cfae", "mathematics", null));
        assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Question isn't full to be created"), response);
    }

    @Test
    public void TestUpdateQuestionPositive() {
        Question question = new Question();
        question.setTitle("vgsw");
        question.setId(1L);
        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));

        ResponseEntity<String> response = questionService.updateQuestion(
                new QuestionRequest("cfae", "mathematics", TypeOfAnswer.ONE_ANSWER), 1L);
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body("Question is updated successfully"), response);

    }

    @Test
    public void TestUpdateQuestionNegativeWrongId() {
        when(questionRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<String> response = questionService.updateQuestion(
                new QuestionRequest("cfae", "mathematics", TypeOfAnswer.ONE_ANSWER), 1L);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question isn't found"), response);
    }

    @Test
    public void TestDeleteQuestionAnswerPositive() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswerText("faqed");
        Question question = new Question();
        question.setTitle("title");
        question.setId(1L);
        question.setAnswers(List.of(answer));
        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        ResponseEntity<String> response = questionService.deleteQuestionsAnswer("title", 1L);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK).body("Answer is deleted successfully");
        assertEquals(expected, response);
    }

    @Test
    public void TestDeleteQuestionAnswerNegativeNotFoundObject() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswerText("faqed");
        Question question = new Question();
        question.setTitle("title");
        question.setId(1L);
        question.setAnswers(List.of(answer));
        when(questionRepository.findById(1L))
                .thenReturn(Optional.empty());
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        ResponseEntity<String> response = questionService.deleteQuestionsAnswer("title", 1L);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question isn't found");
        assertEquals(expected, response);
    }

    @Test
    public void TestGetQuestionPositive() {
        when(questionRepository.findById(any())).thenReturn(Optional.of(
                new Question(1L, "title", null, TypeOfAnswer.STRING, null)));
        ResponseEntity<Question> response = questionService.getQuestion(1L);
        ResponseEntity<Question> expected = ResponseEntity.status(HttpStatus.OK)
                .body(new Question(1L, "title", null, TypeOfAnswer.STRING, null));
        assertEquals(expected, response);
    }

    @Test
    public void TestGetQuestionNegativeNotFoundQuestion() {
        when(questionRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<Question> response = questionService.getQuestion(1L);
        ResponseEntity<Question> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        assertEquals(expected, response);
    }

    @Test
    public void TestDeleteQuestionNegativeNotFoundQuestion() {
        when(questionRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<String> response = questionService.deleteQuestion(1L);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question doesn't exist");
        assertEquals(expected, response);
    }

    @Test
    public void TestDeleteQuestionPositive() {
        when(questionRepository.findById(any())).thenReturn(Optional.of(
                new Question(1L, "title", null, TypeOfAnswer.STRING, null)));
        ResponseEntity<String> response = questionService.deleteQuestion(1L);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK)
                .body(new Question(1L, "title", null, TypeOfAnswer.STRING, null).toString());
        assertEquals(expected, response);
    }


    @Test
    public void TestDeleteQuestionAnswerNegativeNotFoundAnswerInQuestion() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setAnswerText("faqed");
        Question question = new Question();
        question.setTitle("title");
        question.setId(1L);
        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        ResponseEntity<String> response = questionService.deleteQuestionsAnswer("title", 1L);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question doesn't contain this answer");
        assertEquals(expected, response);
    }
}
