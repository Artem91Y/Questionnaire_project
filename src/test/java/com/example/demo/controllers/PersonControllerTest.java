package com.example.demo.controllers;

import com.example.demo.dto.PersonRequest;
import com.example.demo.dto.QuestionRequest;
import com.example.demo.models.Person;
import com.example.demo.models.Questionnaire;
import com.example.demo.models.enums.TypeOfAnswer;
import com.example.demo.repos.PersonRepository;
import com.example.demo.services.PersonService;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;


    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestSavePersonPositive() throws Exception {
        PersonRequest personRequest = new PersonRequest("Nicolas", "admin");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("Person is created successfully");
        when(personService.savePerson(personRequest)).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/addPerson").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person is created successfully"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestSavePersonNegativeNotFullObject() throws Exception {
        PersonRequest personRequest = new PersonRequest(null, "admin");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Person isn't full to be saved");
        when(personService.savePerson(personRequest)).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/addPerson").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Person isn't full to be saved"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestUpdatePersonPositive() throws Exception {
        PersonRequest personRequest = new PersonRequest("Nicolas", "admin");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("Person is updated successfully");
        when(personService.updatePerson(anyString(), any(PersonRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.put("/updatePerson").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)).param("fullName", "Nicolas"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person is updated successfully"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestUpdatePersonNegativeNotFoundObject() throws Exception {
        PersonRequest personRequest = new PersonRequest("Nicolas", "admin");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Nicolas isn't found");
        when(personService.updatePerson(anyString(), any(PersonRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.put("/updatePerson").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)).param("fullName", "Nicolas"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Nicolas isn't found"));
    }


    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestDeletePersonPositive() throws Exception {
        Person person = new Person(1L, "Nicolas", "admin", null);
        ResponseEntity<Person> response = ResponseEntity.status(HttpStatus.OK).body(person);
        when(personService.deletePerson(anyString())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.delete("/deletePerson").param("fullName", "Nicolas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(person)));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestDeletePersonNegativeWrongName() throws Exception {
        ResponseEntity<Person> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(personService.deletePerson(anyString())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.delete("/deletePerson").param("fullName", "someWrongName"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestPassQuestionnairePositive() throws Exception {
        Person person = new Person(1L, "Nicolas", "admin", null);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("Questionnaire was passed successfully");
        when(personRepository.findByUsername(anyString())).thenReturn(Optional.of(person));
        when(personService.passQuestionnaire(anyLong(), anyList())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.put("/passQuestionnaire/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString((List.of("answer1", "answer2")))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Questionnaire was passed successfully"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestPassQuestionnaireNegativeTooManyAnswers() throws Exception {
        Person person = new Person(1L, "Nicolas", "admin", null);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You gave too many answers");
        when(personRepository.findByUsername(anyString())).thenReturn(Optional.of(person));
        when(personService.passQuestionnaire(anyLong(), anyList())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.put("/passQuestionnaire/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString((List.of("answer1", "answer2")))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("You gave too many answers"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestPassQuestionnaireNegativeRepetitivePass() throws Exception {
        Person person = new Person(1L, "Nicolas", "admin", null);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You have already passed this questionnaire");
        when(personRepository.findByUsername(anyString())).thenReturn(Optional.of(person));
        when(personService.passQuestionnaire(anyLong(), anyList())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.put("/passQuestionnaire/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString((List.of("answer1", "answer2")))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("You have already passed this questionnaire"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestGetPassedQuestionnairesWithDetailsPositive() throws Exception {
        Person person = new Person(1L, "Nicolas", "admin", null);
        Map<String, Map<QuestionRequest, String>> result =
                Map.of(
                        "questionnaireName",
                        Map.of(
                                new QuestionRequest("title", "questionnaireName", TypeOfAnswer.STRING),
                                "answer"));
        ResponseEntity<Map<String, Map<QuestionRequest, String>>> response = ResponseEntity.status(HttpStatus.OK).body(result);
        when(personRepository.findByUsername(anyString())).thenReturn(Optional.of(person));
        when(personService.getPassedQuestionnairesWithDetails()).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/getPassedQuestionnairesWithDetails"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(result)));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = {"ADMIN"})
    public void TestGetPassedQuestionnairesWithDetailsNegativeWrongName() throws Exception {
        Person person = new Person(1L, "Nicolas", "admin", null);
        ResponseEntity<Map<String, Map<QuestionRequest, String>>> response =
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(personRepository.findByUsername(anyString())).thenReturn(Optional.of(person));
        when(personService.getPassedQuestionnairesWithDetails()).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/getPassedQuestionnairesWithDetails"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

}
