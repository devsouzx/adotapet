package com.devsouzx.adotapet.controller.adotante.impl;

import com.devsouzx.adotapet.controller.adotante.IAdotanteController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdotanteRequest;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import com.devsouzx.adotapet.service.adotante.IAdotanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/adotante")
@RequiredArgsConstructor
public class AdotanteController implements IAdotanteController {
    private final IAdotanteService iAdotanteService;

    @GetMapping
    public ResponseEntity<Page<AdotanteResponse>> getAdotantes(Integer page, Integer size) {
        Page<AdotanteResponse> adotantes = iAdotanteService.getAdotantes(page, size);
        return ResponseEntity.ok(adotantes);
    }

    @PostMapping("/novo")
    public ResponseEntity<AdotanteResponse> createAdotante(@AuthenticationPrincipal Abrigo abrigo, @RequestBody AdotanteRequest adotanteRequest) {
        AdotanteResponse adotanteResponse = iAdotanteService.createAdotante(adotanteRequest);
        return ResponseEntity.ok(adotanteResponse);
    }

    @GetMapping("/{adotanteId}")
    public ResponseEntity<AdotanteResponse> getAdotanteById(@PathVariable UUID adotanteId) {
        AdotanteResponse adotanteResponse = iAdotanteService.getAdotanteById(adotanteId);
        return ResponseEntity.ok(adotanteResponse);
    }


}
