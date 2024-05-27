package com.example.demo.controllers;

import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Question;
import com.example.demo.models.Questionnaire;
import com.example.demo.models.enums.TypeOfAnswer;
import com.example.demo.services.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestGetQuestionEndpointNegativeWrongId() throws Exception {
        when(questionService.getQuestion(any())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        mockMvc.perform(MockMvcRequestBuilders.get("/getQuestion/-1")) // type of method and url
                .andExpect(MockMvcResultMatchers.status().isNotFound());// expected status

    }


    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestGetQuestionEndpointPositive() throws Exception {
        Question question = new Question();
        question.setId(1L);
        question.setAnswers(new ArrayList<>());
        question.setTitle("title");
        question.setType(TypeOfAnswer.STRING);
        question.setQuestionnaire(new Questionnaire());
        ResponseEntity<Question> response = ResponseEntity.status(HttpStatus.OK).body(question);
        when(questionService.getQuestion(any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/getQuestion").contentType(MediaType.APPLICATION_JSON).param("title", "title"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(question.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(question.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(question.getType().toString()));

    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestDeleteQuestionsAnswerEndpointPositive() throws Exception {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("Answer is deleted successfully");
        when(questionService.deleteQuestionsAnswer(any(), any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAnswer/1")
                        .contentType(MediaType.APPLICATION_JSON).param("title", "title"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Answer is deleted successfully"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestAddQuestionEndpointPositive() throws Exception {
        when(questionService.addQuestion(any(QuestionRequest.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Question is created successfully"));
        mockMvc.perform(MockMvcRequestBuilders.post("/addQuestion")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper
                                .writeValueAsString(new QuestionRequest("title", "name", null))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Question is created successfully"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestUpdateQuestionEndpointPositive() throws Exception {
        when(questionService.updateQuestion(any(QuestionRequest.class), eq("title"))).thenReturn(
                ResponseEntity.status(HttpStatus.CREATED).body("Question is updated successfully"));
        mockMvc.perform(MockMvcRequestBuilders.put("/updateQuestion").param("title", "title").contentType(MediaType.APPLICATION_JSON).content(objectMapper
                    .writeValueAsString(new QuestionRequest("title", "name", null))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(("Question is updated successfully")));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestUpdateQuestionEndpointNegativeNotFound() throws Exception {
        when(questionService.updateQuestion(any(QuestionRequest.class), eq("title"))).thenReturn(
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question isn't found"));
        mockMvc.perform(MockMvcRequestBuilders.put("/updateQuestion").param("title", "title").contentType(MediaType.APPLICATION_JSON).content(objectMapper
                        .writeValueAsString(new QuestionRequest("title", "name", null))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(("Question isn't found")));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestUpdateQuestionEndpointNegativeException() throws Exception {
        when(questionService.updateQuestion(any(QuestionRequest.class), eq("title"))).thenReturn(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Question isn't saved"));
        mockMvc.perform(MockMvcRequestBuilders.put("/updateQuestion").param("title", "title").contentType(MediaType.APPLICATION_JSON).content(objectMapper
                        .writeValueAsString(new QuestionRequest("title", "name", null))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string(("Question isn't saved")));
    }

}
