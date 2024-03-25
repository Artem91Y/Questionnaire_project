package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "person1")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "questionsPassed")
@EqualsAndHashCode
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", unique = true)
    private String fullName;

    private String email;

    @Column(unique = true)
    private String username;

    @OneToMany
    private List<Question> questionsPassed;

}
