package com.example.demo;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.repos.RoleRepository;
import com.example.demo.repos.UserRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DemoApplication{

		public static void main(String[] args) {
			SpringApplication.run(DemoApplication.class, args);
		}

		@Bean
		CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
			return args -> {
				if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
				Role adminRole = roleRepository.save(new Role("ADMIN"));
				if (roleRepository.findByAuthority("EMPLOYEE").isEmpty()){
					roleRepository.save(new Role("EMPLOYEE"));
				}
				if (roleRepository.findByAuthority("USER").isEmpty()){
					roleRepository.save(new Role("USER"));
				}

				Set<Role> roles = new HashSet<>();
				roles.add(adminRole);

				User admin = new User(1L, passwordEncoder.encode("password"), "admin", roles);

				userRepository.save(admin);
			};
		}



}
