package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/auth/**", "/error").permitAll()

                // ADMIN only
                .requestMatchers("/users/**").hasRole("ADMIN")

                // ADMIN and OWNER
                .requestMatchers("/venues/**").hasAnyRole("ADMIN", "OWNER")
                .requestMatchers("/regulations/**").hasAnyRole("ADMIN", "OWNER")
                .requestMatchers("/exception-rules/**").hasAnyRole("ADMIN", "OWNER")

                // ADMIN and CUSTOMER
                .requestMatchers("/bookings/**").hasAnyRole("ADMIN", "CUSTOMER")

                // anyRequest MUST always be last!
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}