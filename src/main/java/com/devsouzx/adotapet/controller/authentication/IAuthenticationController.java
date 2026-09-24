package com.devsouzx.adotapet.controller.authentication;

import com.devsouzx.adotapet.dto.request.LoginRequest;
import com.devsouzx.adotapet.dto.request.RegisterRequest;
import com.devsouzx.adotapet.dto.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthenticationController {
    ResponseEntity<AuthenticationResponse> login(LoginRequest request) throws Exception;
    ResponseEntity<AuthenticationResponse> register(RegisterRequest request);
}
