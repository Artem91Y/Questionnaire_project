package com.example.demo.services;

import com.example.demo.models.Answer;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repos.AnswerRepository;
import com.example.demo.repos.RoleRepository;
import com.example.demo.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserServiceTest() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testSaveUserPositive() {
        when(userRepository.findByUserName("smith1"))
                .thenReturn(Optional.empty());
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(new Role(1L, "USER")));
        when(bCryptPasswordEncoder.encode("gwrefas")).thenReturn("ugbjliko");
        boolean response = userService.saveUser(new User(null, "gwrefas", "smith1", new HashSet<>()), Map.of("gvlrpe", "USER"));
        assertEquals(true, response);
    }

    @Test
    public void testSaveUserNegativeBecauseOfAdminRole() {
        when(userRepository.findByUserName("smith1"))
                .thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("gwrefas")).thenReturn("ugbjliko");
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(new Role(1L, "USER")));
        boolean response = userService.saveUser(new User(null, "gwrefas", "smith1", new HashSet<>()), Map.of("gvlrpe", "ADMIN"));
        assertEquals(false, response);
    }
    @Test
    public void testSaveUserNegativeBecauseOfEmptyRole() {
        when(userRepository.findByUserName("smith1"))
                .thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("gwrefas")).thenReturn("ugbjliko");
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(new Role(1L, "USER")));

        boolean response = userService.saveUser(new User(null, "gwrefas", "smith1", new HashSet<>()), new HashMap<>());
        assertEquals(false, response);
    }

    @Test
    public void testSaveUserNegativeBecauseOfTheSameUsername() {
        when(userRepository.findByUserName("smith1"))
                .thenReturn(Optional.of(new User(1L, "gwrefas", "smith1", new HashSet<>())));
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(new Role(1L, "USER")));
        when(bCryptPasswordEncoder.encode("gwrefas")).thenReturn("ugbjliko");
        boolean response = userService.saveUser(new User(null, "gwrefas", "smith1", new HashSet<>()), Map.of("gvlrpe", "USER"));
        assertEquals(false, response);
    }


}
