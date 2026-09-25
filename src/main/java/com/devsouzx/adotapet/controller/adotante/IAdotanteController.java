package com.devsouzx.adotapet.controller.adotante;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdotanteRequest;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IAdotanteController {
    ResponseEntity<Page<AdotanteResponse>> getAdotantes(Integer page, Integer size);
    ResponseEntity<AdotanteResponse> createAdotante(Abrigo abrigo, AdotanteRequest adotanteRequest);
    ResponseEntity<AdotanteResponse> getAdotanteById(UUID adotanteId);
}
