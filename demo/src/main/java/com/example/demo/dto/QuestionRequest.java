package com.example.demo.dto;

import com.example.demo.models.Questionnaire;
import com.example.demo.models.enums.TypeOfAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class QuestionRequest {

    private String name;


    private Questionnaire questionnaire;

    private TypeOfAnswer type;

    private Map<Long, String> personIdToAnswer;
}
