package com.example.demo.repositories;

import com.example.demo.models.Questionnaire;
import com.example.demo.repos.QuestionnaireRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuestionnaireRepositoryTest {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Test
    public void findActiveQuestionnairesTest(){
        LocalDate startTime = LocalDate.of(2023, 1,1);
        LocalDate endTime = LocalDate.of(LocalDate.now().getYear() + 1, 1, 1);
        Questionnaire questionnaire = new Questionnaire(1L, "name", "description", startTime, endTime, null);
        Questionnaire questionnaire2 = new Questionnaire(2L, "name", "description", endTime, endTime, null);
        questionnaireRepository.save(questionnaire);
        questionnaireRepository.save(questionnaire2);
        List<Questionnaire> active = questionnaireRepository.findActiveQuestionnaires(LocalDate.now());
        assertEquals(active, List.of(questionnaire));
    }
}
