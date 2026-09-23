package com.devsouzx.adotapet.service.abrigo;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.RegisterRequest;
import com.devsouzx.adotapet.dto.response.AbrigoInfoResponse;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface IAbrigoService {
    Abrigo getAbrigoByEmail(String email) throws Exception;
    Abrigo getAbrigoById(UUID identifier) throws Exception;
    AbrigoInfoResponse getAbrigoInfoById(UUID id) throws Exception;
    AbrigoInfoResponse updateAbrigo(UUID id, AbrigoUpdateRequest abrigoUpdateRequest) throws Exception;
    List<AbrigoInfoResponse> getAbrigosProximos(double latitude, double longitude, double raio);
    Abrigo salvarAbrigo(RegisterRequest request);
    AbrigoInfoResponse toResponse(Abrigo abrigo);
}
