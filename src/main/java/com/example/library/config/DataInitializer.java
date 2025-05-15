package com.example.library.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    // Criar usuário admin
    if (userRepository.findByUsername("admin").isEmpty()) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER"));
      userRepository.save(admin);
      System.out.println("Usuário admin criado com sucesso!");
    }

    // Criar usuário comum
    if (userRepository.findByUsername("user").isEmpty()) {
      User user = new User();
      user.setUsername("user");
      user.setPassword(passwordEncoder.encode("user123"));
      user.setRoles(Set.of("ROLE_USER"));
      userRepository.save(user);
      System.out.println("Usuário comum criado com sucesso!");
    }
  }
}