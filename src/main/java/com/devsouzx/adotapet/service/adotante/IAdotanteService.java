package com.devsouzx.adotapet.service.adotante;

import com.devsouzx.adotapet.domain.adotante.Adotante;
import com.devsouzx.adotapet.dto.request.AdotanteRequest;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IAdotanteService {
    Page<AdotanteResponse> getAdotantes(Integer page, Integer size);
    AdotanteResponse toResponse(Adotante adotante);
    AdotanteResponse createAdotante(AdotanteRequest adotanteRequest);
    AdotanteResponse getAdotanteById(UUID adotanteId);
}
