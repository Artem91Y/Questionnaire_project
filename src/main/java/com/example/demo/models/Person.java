package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "questionnairesPassed")
@EqualsAndHashCode
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", unique = true)
    private String fullName;


    @Column(unique = true)
    private String username;

    @ManyToMany
//    @JoinColumn(name = "passed_questionnaires_ids")
    private Set<Questionnaire> questionnairesPassed;

}
