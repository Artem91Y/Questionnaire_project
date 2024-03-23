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

    private boolean NotAdminOrEmployee(Authentication authentication) {
        return !(authentication.getName().equals("ADMIN"));
    }

    public boolean hasPermission(Long id) {
        Authentication authentication = getUserFromContext();
        if (NotAdminOrEmployee(authentication)) {
            return personRepository.findById(id).get().getUsername().equals(authentication.getName());
        }
        return true;
    }


}
