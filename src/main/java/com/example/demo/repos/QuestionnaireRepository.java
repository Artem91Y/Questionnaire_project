package com.example.demo.repos;

import com.example.demo.models.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    Optional<Questionnaire> findByName(String name);

    @Query("SELECT t FROM Questionnaire t WHERE DATEDIFF(t.endTime, :now) > 0 AND DATEDIFF(:now, t.startTime) > 0")
    List<Questionnaire> findActiveQuestionnaires(@Param("now") LocalDate now);
}
