package com.devsouzx.adotapet.controller.authentication.impl;

import com.devsouzx.adotapet.controller.authentication.IAuthenticationController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.LoginRequest;
import com.devsouzx.adotapet.dto.request.RegisterRequest;
import com.devsouzx.adotapet.dto.request.UserRequestResetPasswordRequest;
import com.devsouzx.adotapet.dto.request.UserResetPasswordRequest;
import com.devsouzx.adotapet.dto.response.AuthenticationResponse;
import com.devsouzx.adotapet.exception.InvalidCredentialsException;
import com.devsouzx.adotapet.infra.config.TokenService;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
import com.devsouzx.adotapet.service.authentication.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController implements IAuthenticationController {
    private final IAuthenticationService iAuthenticationService;
    private final IAbrigoService iAbrigoService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        Abrigo abrigo = iAbrigoService.findAbrigoByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.senha(), abrigo.getSenha())) {
            throw new InvalidCredentialsException();
        }
        String token = this.tokenService.generateToken(abrigo);
        return ResponseEntity.ok(new AuthenticationResponse(abrigo.getNome(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        Abrigo abrigo = iAbrigoService.salvarAbrigo(request);
        String token = this.tokenService.generateToken(abrigo);
        return ResponseEntity.ok(new AuthenticationResponse(abrigo.getNome(), token));
    }

    @PostMapping(value = "/request-password-reset")
    public ResponseEntity<Void> sendRequestPasswordResetEmail(@RequestBody @Valid UserRequestResetPasswordRequest request) {
        iAuthenticationService.sendPassswordResetEmail(request.email());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/resetpassword/")
    public ResponseEntity<Void> resetPassword(@RequestParam("id") UUID id,
                                              @RequestParam("hash") String code,
                                              @RequestBody @Valid UserResetPasswordRequest request) {
        iAuthenticationService.resetPassword(request, id, code);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
