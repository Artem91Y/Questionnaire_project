package com.example.demo.services;

import com.example.demo.dto.PersonRequest;
import com.example.demo.models.Person;
import com.example.demo.models.Question;
import com.example.demo.models.Questionnaire;
import com.example.demo.repos.AnswerRepository;
import com.example.demo.repos.PersonRepository;
import com.example.demo.repos.QuestionnaireRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonServiceTest {
    @InjectMocks
    private PersonService personService;
    @Mock
    private PersonRepository personRepository;

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private Authentication authentication;

    public PersonServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestSavePersonPositive(){
        ResponseEntity<String> response = personService.savePerson(new PersonRequest("vda", "bnjiuk"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.CREATED).body("Person is created successfully");
        assertEquals(response, expected);
    }
    @Test
    public void TestSavePersonNegativeBecauseOfNotFullObject(){
        ResponseEntity<String> response = personService.savePerson(new PersonRequest(null, "bnjiuk"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Person isn't full to be saved");
        assertEquals(response, expected);
    }

    @Test
    public void TestUpdatePersonPositive(){
        when(personRepository.findByFullNameLikeIgnoreCase("fullName")).thenReturn(Optional.of(new Person(1L, "vfdsaw", "fcew", null)));
        ResponseEntity<String> response = personService.updatePerson("fullName", new PersonRequest("vda", "bnjiuk"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.CREATED).body("Person is updated successfully");
        assertEquals(response, expected);
    }

    @Test
    public void TestUpdatePersonNegativeBecauseOfWrongId(){
        when(personRepository.findByFullNameLikeIgnoreCase("fullName")).thenReturn(Optional.empty());
        ResponseEntity<String> response = personService.updatePerson("fullName", new PersonRequest("vda", "bnjiuk"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("%s isn't found", "fullName"));
        assertEquals(response, expected);
    }

    @Test
    public void TestPassQuestionnairePositive(){
        SecurityContext securityContext = mock(SecurityContext.class);
        when(questionnaireRepository.findById(1L)).thenReturn(Optional.of(new Questionnaire(1L, "vfdsaw", "fcew", null, null, List.of(new Question(), new Question()))));
        when(personRepository.findByUsername("smith1")).thenReturn(Optional.of(new Person(2L, "fwr", "smith1", null)));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("smith1");
        ResponseEntity<String> response = personService.passQuestionnaire(1L, List.of("hi", "hello"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK).body("Questionnaire was passed successfully");
        assertEquals(response, expected);
    }

    @Test
    public void TestPassQuestionnaireNegativeNotFoundObject(){
        SecurityContext securityContext = mock(SecurityContext.class);
        when(questionnaireRepository.findById(1L)).thenReturn(Optional.of(new Questionnaire(1L, "vfdsaw", "fcew", null, null, List.of(new Question(), new Question()))));
        when(personRepository.findByUsername("smith1")).thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("smith1");
        ResponseEntity<String> response = personService.passQuestionnaire(1L, List.of("hi", "hello"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person isn't found");
        assertEquals(response, expected);
    }

    @Test
    public void TestPassQuestionnaireNegativeWrongNumberOfAnswers(){
        SecurityContext securityContext = mock(SecurityContext.class);
        when(questionnaireRepository.findById(1L)).thenReturn(Optional.of(new Questionnaire(1L, "vfdsaw", "fcew", null, null, List.of(new Question(), new Question()))));
        when(personRepository.findByUsername("smith1")).thenReturn(Optional.of(new Person(2L, "fwr", "smith1", null)));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("smith1");
        ResponseEntity<String> response = personService.passQuestionnaire(1L, List.of("hi"));
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You haven't answered all the question");
        assertEquals(response, expected);
    }


}
