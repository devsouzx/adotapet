package com.devsouzx.adotapet.service.authentication;

import com.devsouzx.adotapet.dto.request.UserResetPasswordRequest;

import java.util.UUID;

public interface IAuthenticationService {

    void sendPassswordResetEmail(String email) throws Exception;
    void resetPassword(UserResetPasswordRequest request, UUID id, String code) throws Exception;
}
