package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "`answer`")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    private Question question;

    private Person person;

    private String answerText;
}
