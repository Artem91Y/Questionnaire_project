package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repos.RoleRepository;
import com.example.demo.repos.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@NoArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public boolean saveUser(User user, Map<String, String> names) {

        Optional<User> userForComparison = userRepository.findByUserName(user.getUsername());
        if (userForComparison.isPresent()) {
            return false;
        }

        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            Set<Role> roles = new HashSet<>();
            for (String name: names.values()) {
                if (name.equals("ADMIN")){
                    return false;
                }
                Optional<Role> role = roleRepository.findByAuthority(name);
                if (role.isEmpty()){
                    return false;
                }
                roles.add(role.get());
            }
            user.setAuthorities(roles);

            userRepository.save(user);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public User findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.get() != null) {
            return user.get();
        } else {
            return new User();
        }
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }
}
