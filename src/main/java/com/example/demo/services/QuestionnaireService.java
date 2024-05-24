package com.example.demo.services;

import com.example.demo.dto.QuestionnaireRequest;
import com.example.demo.models.Questionnaire;
import com.example.demo.repos.QuestionnaireRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;


    public QuestionnaireService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    public ResponseEntity<String> addQuestionnaire(QuestionnaireRequest questionnaireFromEndpoint) {
        if (questionnaireFromEndpoint.getDescription() == null
                || questionnaireFromEndpoint.getEndTime() == null
                || questionnaireFromEndpoint.getName() == null
                || questionnaireFromEndpoint.getStartTime() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Questionnaire isn't full to be created");
        }
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(questionnaireFromEndpoint.getName());
        questionnaire.setStartTime(questionnaireFromEndpoint.getStartTime());
        questionnaire.setEndTime(questionnaireFromEndpoint.getEndTime());
        questionnaire.setDescription(questionnaireFromEndpoint.getDescription());
        try {
            questionnaireRepository.save(questionnaire);
            return ResponseEntity.status(HttpStatus.CREATED).body("Questionnaire is created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Questionnaire isn't saved");
        }
    }


    public ResponseEntity<String> updateQuestionnaire(QuestionnaireRequest questionnaire, Long id) {
        Optional<Questionnaire> questionnaireForRemoval = questionnaireRepository.findById(id);
        if (questionnaireForRemoval.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionnaire isn't found");
        }
        Questionnaire newQuestionnaire = questionnaireForRemoval.get();
        if (questionnaire.getDescription() != null) {
            newQuestionnaire.setDescription(questionnaire.getDescription());
        }
        if (questionnaire.getName() != null) {
            newQuestionnaire.setName(questionnaire.getName());
        }
        if (questionnaire.getEndTime() != null) {
            newQuestionnaire.setEndTime(questionnaire.getEndTime());
        }
        if (questionnaire.getStartTime() != null) {
            newQuestionnaire.setStartTime(questionnaire.getStartTime());
        }
        try {
            questionnaireRepository.save(newQuestionnaire);
            return ResponseEntity.status(HttpStatus.CREATED).body("Questionnaire is updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Questionnaire isn't updated");
        }
    }

//      TODO remake delete ResponseEntity<String> -> ResponseEntity<Questionnaire>

    public ResponseEntity<String> deleteQuestionnaire(Long id) {
        try {
            if (questionnaireRepository.findById(id).isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionnaire doesn't exist");
            }
            Questionnaire question = questionnaireRepository.findById(id).get();
            questionnaireRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(question.toString());

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete Questionnaire");
        }
    }


//      TODO remake get ResponseEntity<String> -> ResponseEntity<Questionnaire>
    public ResponseEntity<String> getQuestionnaire(Long id) {
        try {
            if (questionnaireRepository.findById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(questionnaireRepository.findById(id).get().toString());
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get Questionnaire");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionnaire isn't found");

    }


    public List<Questionnaire> getActiveQuestionnaires(){
        return questionnaireRepository.findActiveQuestionnaires(LocalDate.now());
    }
}
