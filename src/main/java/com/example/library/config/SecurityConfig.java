package com.example.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.library.repository.UserRepository;
import com.example.library.security.JwtRequestFilter;
import com.example.library.security.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepository.findByUsername(username)
        .map(user -> org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().stream()
                .map(role -> role.replace("ROLE_", ""))
                .toArray(String[]::new))
            .build())
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public JwtRequestFilter jwtRequestFilter(UserDetailsService userDetailsService) {
    JwtRequestFilter filter = new JwtRequestFilter();
    filter.setUserDetailsService(userDetailsService);
    filter.setJwtUtil(jwtUtil);
    return filter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter)
      throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/{id}").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/books/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .formLogin(form -> form.disable())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}