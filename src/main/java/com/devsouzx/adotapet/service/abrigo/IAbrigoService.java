package com.devsouzx.adotapet.service.abrigo;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.response.AbrigoInfoResponse;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;
import com.devsouzx.adotapet.dto.request.UserResetPasswordRequest;

import java.util.List;
import java.util.UUID;

public interface IAbrigoService {
    void sendPassswordResetEmail(String email) throws Exception;
    void resetPassword(UserResetPasswordRequest request, UUID id, String code) throws Exception;
    Abrigo getAbrigoByEmail(String email) throws Exception;
    Abrigo getAbrigoById(UUID identifier) throws Exception;
    AbrigoInfoResponse getAbrigoInfoById(UUID id) throws Exception;
    AbrigoInfoResponse updateAbrigoInfo(UUID id, AbrigoUpdateRequest abrigoUpdateRequest) throws Exception;
    List<AbrigoInfoResponse> getAbrigosProximos(double latitude, double longitude, double raio);
}
