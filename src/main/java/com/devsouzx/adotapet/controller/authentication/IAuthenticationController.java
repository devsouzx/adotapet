package com.devsouzx.adotapet.controller.authentication;

import com.devsouzx.adotapet.dto.request.LoginRequest;
import com.devsouzx.adotapet.dto.request.RegisterRequest;
import com.devsouzx.adotapet.dto.response.AuthenticationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IAuthenticationController {
    ResponseEntity<AuthenticationResponse> login(LoginRequest request) throws Exception;
    ResponseEntity<AuthenticationResponse> register(RegisterRequest request);
}
