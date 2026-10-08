package com.devsouzx.adotapet.infra.config;

import com.devsouzx.adotapet.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilterChain;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper objectMapper) {
        http
                .csrf(
                        AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pet", "/pet/filtros", "/pet/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/abrigo/*", "/abrigo/proximos").permitAll()
                        .requestMatchers("/adocao", "/adocao/**").authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            writeErrorResponse(response, objectMapper, ApiErrorResponse.of(
                                    HttpStatus.UNAUTHORIZED.value(),
                                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                    "UNAUTHENTICATED",
                                    "Autenticação necessária ou token inválido",
                                    request.getRequestURI(),
                                    Map.of()
                            ));
                        })
                        .accessDeniedHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            writeErrorResponse(response, objectMapper, ApiErrorResponse.of(
                                    HttpStatus.FORBIDDEN.value(),
                                    HttpStatus.FORBIDDEN.getReasonPhrase(),
                                    "FORBIDDEN",
                                    "Você não tem permissão para acessar este recurso",
                                    request.getRequestURI(),
                                    Map.of()
                            ));
                        }))
                .addFilterBefore(
                        jwtFilterChain,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http
                .build();
    }

    private void writeErrorResponse(
            HttpServletResponse response,
            ObjectMapper objectMapper,
            ApiErrorResponse error
    ) throws IOException {
        try {
            objectMapper.writeValue(response.getOutputStream(), error);
        } catch (JacksonException exception) {
            throw new IOException("Falha ao serializar a resposta de erro de segurança", exception);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
