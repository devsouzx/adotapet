package com.devsouzx.adotapet.service.abrigo;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.RegisterRequest;
import com.devsouzx.adotapet.dto.response.AbrigoInfoResponse;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface IAbrigoService {
    Optional<Abrigo> findAbrigoByEmail(String email);
    Abrigo getAbrigoByEmail(String email);
    Abrigo getAbrigoById(UUID identifier);
    AbrigoInfoResponse getAbrigoInfoById(UUID id);
    AbrigoInfoResponse updateAbrigo(UUID id, AbrigoUpdateRequest abrigoUpdateRequest);
    Page<AbrigoInfoResponse> getAbrigosProximos(double latitude, double longitude, double raio, Integer page, Integer size);
    Abrigo salvarAbrigo(RegisterRequest request);
    AbrigoInfoResponse toResponse(Abrigo abrigo);
}
