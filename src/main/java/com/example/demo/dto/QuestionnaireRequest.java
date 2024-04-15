package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class QuestionnaireRequest {

    private LocalDate startTime;

    private LocalDate endTime;

    private  String name;

    private String description;
}
