package com.example.demo.dto;

import com.example.demo.models.Answer;
import com.example.demo.models.Questionnaire;
import com.example.demo.models.enums.TypeOfAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionRequest {

    private String title;

    private String questionnaireName;

    private TypeOfAnswer type;
}
