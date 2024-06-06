package com.example.demo.repositories;

import com.example.demo.config.SecurityConfig;
import com.example.demo.models.Questionnaire;
import com.example.demo.repos.QuestionnaireRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(SecurityConfig.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class QuestionnaireRepositoryTest {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void findActiveQuestionnairesTestPositive() {
        LocalDate startTime = LocalDate.of(2023, 1, 1);
        LocalDate endTime = LocalDate.of(LocalDate.now().getYear() + 1, 1, 1);
        Questionnaire questionnaire = new Questionnaire(1L, "name", "description", startTime, endTime, null);
        Questionnaire questionnaire2 = new Questionnaire(2L, "name2", "description", endTime, endTime, null);
        questionnaireRepository.save(questionnaire);
        questionnaireRepository.save(questionnaire2);
//        ArrayList<Questionnaire> expected = new ArrayList(List.of(questionnaire));
        assertThat(questionnaireRepository.findActiveQuestionnaires(LocalDate.now())).hasSize(1).isSameAs(new ArrayList(List.of(questionnaire)));
    }
}
