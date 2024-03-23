package com.example.demo.repos;

import com.example.demo.models.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long>{
}
