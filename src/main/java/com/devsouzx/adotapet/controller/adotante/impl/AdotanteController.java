package com.devsouzx.adotapet.controller.adotante.impl;

import com.devsouzx.adotapet.controller.adotante.IAdotanteController;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import com.devsouzx.adotapet.service.adotante.IAdotanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
