package com.devsouzx.adotapet.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilterChain;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(
                        AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pet", "/pet/filtros", "/pet/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/abrigo/*", "/abrigo/proximos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/adotante", "/adotante/*").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(
                        jwtFilterChain,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http
                .build();
    }
}
