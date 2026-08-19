package com.devsouzx.adotapet.controller;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.AuthenticationResponseDTO;
import com.devsouzx.adotapet.dto.LoginRequestDTO;
import com.devsouzx.adotapet.dto.RegisterRequestDTO;
import com.devsouzx.adotapet.dto.UserPasswordUpdateRequest;
import com.devsouzx.adotapet.infra.config.TokenService;
import com.devsouzx.adotapet.service.AbrigoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AbrigoService abrigoService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        Abrigo abrigo = abrigoService.findByEmail(request.email());
        if (passwordEncoder.matches(request.senha(), abrigo.getSenha())) {
            String token = this.tokenService.generateToken(abrigo);
            return ResponseEntity.ok(new AuthenticationResponseDTO(abrigo.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/cadastro")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        Abrigo abrigo = abrigoService.salvarAbrigoDTO(request);
        String token = this.tokenService.generateToken(abrigo);
        return ResponseEntity.ok(new AuthenticationResponseDTO(abrigo.getNome(), token));
    }


    @PostMapping("/atualizar-senha")
    public ResponseEntity<?> recuperarSenha(
            @AuthenticationPrincipal Abrigo abrigo,
            @RequestBody UserPasswordUpdateRequest request
            ) {
        UUID userIdentifier = abrigo.getId();
        abrigoService.atualizarSenha(request, userIdentifier);
        return ResponseEntity.ok("Senha atualizada");
    }
}
