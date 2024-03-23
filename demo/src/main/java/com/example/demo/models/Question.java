package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "Question")
@NoArgsConstructor
@Setter
@Getter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private Questionnaire questionnaire;

    private TypeOfAnswer type;

    private Map<Long, String> personIdToAnswer;

}
