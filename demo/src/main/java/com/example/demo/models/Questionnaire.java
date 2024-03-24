package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "questionnaire")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "questions")
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @OneToMany(mappedBy = "questionnaire")
    private List<Question> questions;
}
