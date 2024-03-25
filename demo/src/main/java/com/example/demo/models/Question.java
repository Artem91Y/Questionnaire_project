package com.example.demo.models;

import com.example.demo.models.enums.TypeOfAnswer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Question")
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "questionnaire")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private Questionnaire questionnaire;

    private TypeOfAnswer type;

    @OneToMany
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private List<Answer> answers;

}
