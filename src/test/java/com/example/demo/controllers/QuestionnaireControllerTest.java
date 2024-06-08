package com.example.demo.controllers;

import com.example.demo.dto.QuestionnaireRequest;
import com.example.demo.models.Questionnaire;
import com.example.demo.services.QuestionnaireService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionnaireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionnaireService questionnaireService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestAddQuestionnairePositive() throws Exception {
        QuestionnaireRequest questionnaire = new QuestionnaireRequest(
                LocalDate.of(1000, 5, 1),
                LocalDate.of(LocalDate.EPOCH.getYear() + 1, 1, 1),
                "name",
                "description");
        when(questionnaireService.addQuestionnaire(any(QuestionnaireRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Questionnaire is created successfully"));
        mockMvc.perform(MockMvcRequestBuilders.post("/addQuestionnaire").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionnaire)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Questionnaire is created successfully"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestAddQuestionnaireNegativeNotFullObject() throws Exception {
        QuestionnaireRequest questionnaire = new QuestionnaireRequest(
                LocalDate.of(1000, 5, 1),
                LocalDate.of(LocalDate.EPOCH.getYear() + 1, 1, 1),
                "name",
                "description");
        when(questionnaireService.addQuestionnaire(any(QuestionnaireRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Questionnaire isn't full to be created"));
        mockMvc.perform(MockMvcRequestBuilders.post("/addQuestionnaire").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionnaire)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Questionnaire isn't full to be created"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestUpdateQuestionnairePositive() throws Exception {
        QuestionnaireRequest questionnaire = new QuestionnaireRequest(
                LocalDate.of(1000, 5, 1),
                LocalDate.of(LocalDate.EPOCH.getYear() + 1, 1, 1),
                "name",
                "description");
        when(questionnaireService.updateQuestionnaire(any(QuestionnaireRequest.class), anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Questionnaire is updated successfully"));
        mockMvc.perform(MockMvcRequestBuilders.put("/updateQuestionnaire").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionnaire)).param("name", "name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Questionnaire is updated successfully"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestUpdateQuestionnaireNegativeNotFoundQuestionnaire() throws Exception {
        QuestionnaireRequest questionnaire = new QuestionnaireRequest(
                LocalDate.of(1000, 5, 1),
                LocalDate.of(LocalDate.EPOCH.getYear() + 1, 1, 1),
                "name",
                "description");
        when(questionnaireService.updateQuestionnaire(any(QuestionnaireRequest.class), anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionnaire isn't found"));
        mockMvc.perform(MockMvcRequestBuilders.put("/updateQuestionnaire").param("name", "name").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionnaire)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Questionnaire isn't found"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestDeleteQuestionnairePositive() throws Exception {
        Questionnaire questionnaire = new Questionnaire(
                1L,
                "name",
                "description",
                LocalDate.of(1000, 5, 1),
                LocalDate.of(LocalDate.EPOCH.getYear() + 1, 1, 1),
                null);
        when(questionnaireService.deleteQuestionnaire("name")).thenReturn(ResponseEntity.status(HttpStatus.OK).body(questionnaire));
        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteQuestionnaire").param("name", "name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(questionnaire)));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestDeleteQuestionnaireNegativeNotFoundQuestionnaire() throws Exception {
        when(questionnaireService.deleteQuestionnaire("name")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteQuestionnaire").param("name", "name"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestGetQuestionnairePositive() throws Exception {
        Questionnaire questionnaire = new Questionnaire(
                1L,
                "name",
                "description",
                LocalDate.of(1000, 5, 1),
                LocalDate.of(LocalDate.EPOCH.getYear() + 1, 1, 1),
                null);
        when(questionnaireService.getQuestionnaire("name")).thenReturn(ResponseEntity.status(HttpStatus.OK).body(questionnaire));
        mockMvc.perform(MockMvcRequestBuilders.get("/getQuestionnaire").param("name", "name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(questionnaire)));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestGetQuestionnaireNegativeNotFoundQuestionnaire() throws Exception {
        when(questionnaireService.getQuestionnaire("name")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        mockMvc.perform(MockMvcRequestBuilders.get("/getQuestionnaire").param("name", "name"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestGetActiveQuestionnairesPositive() throws Exception {
        Questionnaire questionnaire = new Questionnaire(
                1L,
                "name",
                "description",
                LocalDate.of(1000, 5, 1),
                LocalDate.of(LocalDate.EPOCH.getYear() + 1, 1, 1),
                null);
        ResponseEntity<List<Questionnaire>> response = ResponseEntity.status(HttpStatus.OK).body(List.of(questionnaire));
        when(questionnaireService.getActiveQuestionnaires()).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/getActiveQuestionnaires").param("name", "name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(List.of(questionnaire))));
    }
}
