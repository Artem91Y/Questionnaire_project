package com.example.demo.services;

import com.example.demo.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CheckPermissions {

    @Autowired
    private final PersonRepository personRepository;


    public CheckPermissions(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private Authentication getUserFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public boolean hasPermission(Long id) {
        Authentication authentication = getUserFromContext();
        if (!(authentication.getName().equals("ADMIN"))) {
            return personRepository.findById(id).get().getUsername().equals(authentication.getName());
        }
        return true;
    }


}
