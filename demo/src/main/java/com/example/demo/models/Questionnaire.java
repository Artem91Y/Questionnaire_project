package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "questionnaire")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "question")
    private List<Question> questions;
}
