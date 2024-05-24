package com.example.demo.services;

import com.example.demo.dto.QuestionnaireRequest;
import com.example.demo.models.Questionnaire;
import com.example.demo.repos.QuestionnaireRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class QuestionnaireServiceTest {
    @InjectMocks
    private QuestionnaireService questionnaireService;

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    public QuestionnaireServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestAddQuestionnairePositive(){
        ResponseEntity<String> response = questionnaireService.addQuestionnaire(new QuestionnaireRequest(LocalDate.of(2020, 12, 1), LocalDate.now(), "vda", "bnjiuk"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.CREATED).body("Questionnaire is created successfully");
        assertEquals(response, expected);
    }

    @Test
    public void TestAddQuestionnaireNegativeBecauseOfNotFullObject(){
        ResponseEntity<String> response = questionnaireService.addQuestionnaire(new QuestionnaireRequest(LocalDate.of(2020, 12, 1), null, "vda", "bnjiuk"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Questionnaire isn't full to be created");
        assertEquals(response, expected);
    }

    @Test
    public void TestUpdateQuestionnairePositive(){
        when(questionnaireRepository.findByName(any())).thenReturn(Optional.of(new Questionnaire(1L, "vds", "vgfds", null, null, null)));
        ResponseEntity<String> response = questionnaireService.updateQuestionnaire(new QuestionnaireRequest(LocalDate.of(2020, 12, 1), LocalDate.now(), "vda", "bnjiuk"), "name");
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.CREATED).body("Questionnaire is updated successfully");
        assertEquals(response, expected);
    }

    @Test
    public void TestUpdateQuestionnaireNegativeBecauseOfWrongId(){
        when(questionnaireRepository.findByName(any())).thenReturn(Optional.empty());
        ResponseEntity<String> response = questionnaireService.updateQuestionnaire(new QuestionnaireRequest(LocalDate.of(2020, 12, 1), LocalDate.now(), "vda", "bnjiuk"), "name");
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionnaire isn't found");
        assertEquals(response, expected);
    }






}
