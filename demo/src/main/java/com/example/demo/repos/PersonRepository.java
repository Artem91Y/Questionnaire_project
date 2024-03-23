package com.example.demo.repos;

import com.example.demo.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository
        extends JpaRepository<Person, Long> {
    List<Person> findByFullNameLikeIgnoreCase(String fullName);

    List<Person> findByEmailLikeIgnoreCase(String email);

}
