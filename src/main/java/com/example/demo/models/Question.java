package com.example.demo.models;

import com.example.demo.models.enums.TypeOfAnswer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Question")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = "questionnaire")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

    private TypeOfAnswer type;

    @OneToMany
    @JsonBackReference
    @JoinColumn(name = "question_id")
    private List<Answer> answers;

}
