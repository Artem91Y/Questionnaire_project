package com.example.demo.dto;

import com.example.demo.models.enums.TypeOfAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionRequest {

    private String title;

    private String questionnaireName;

    private TypeOfAnswer type;
}
