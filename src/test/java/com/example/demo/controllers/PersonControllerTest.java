package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void TestAddPersonEndpoint() throws Exception{
        String requestBody = "{\n" +
                "    \"fullName\": \"smith\",\n" +
                "    \"username\": \"admin\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addPerson").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Person is created successfully"));

    }

    public void TestGetPersonEndpoint() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/getPassedQuestionnairesWithDetails")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("key").value("value"));

    }
}
