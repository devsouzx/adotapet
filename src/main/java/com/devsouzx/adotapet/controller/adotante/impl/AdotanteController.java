package com.devsouzx.adotapet.controller.adotante.impl;

import com.devsouzx.adotapet.controller.adotante.IAdotanteController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdotanteRequest;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import com.devsouzx.adotapet.service.adotante.IAdotanteService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/adotante")
@RequiredArgsConstructor
@Tag(name = "Adotantes", description = "Cadastro e consulta de adotantes.")
public class AdotanteController implements IAdotanteController {
    private final IAdotanteService iAdotanteService;

    @GetMapping
    @Operation(summary = "Listar adotantes")
    public ResponseEntity<Page<AdotanteResponse>> getAdotantes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<AdotanteResponse> adotantes = iAdotanteService.getAdotantes(page, size);
        return ResponseEntity.ok(adotantes);
    }

    @PostMapping("/novo")
    @Operation(summary = "Cadastrar adotante")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AdotanteResponse> createAdotante(@AuthenticationPrincipal Abrigo abrigo, @RequestBody @Valid AdotanteRequest adotanteRequest) {
        AdotanteResponse adotanteResponse = iAdotanteService.createAdotante(adotanteRequest);
        return ResponseEntity.ok(adotanteResponse);
    }

    @GetMapping("/{adotanteId}")
    @Operation(summary = "Consultar adotante por identificador")
    public ResponseEntity<AdotanteResponse> getAdotanteById(@PathVariable UUID adotanteId) {
        AdotanteResponse adotanteResponse = iAdotanteService.getAdotanteById(adotanteId);
        return ResponseEntity.ok(adotanteResponse);
    }

    @PutMapping("/{adotanteId}")
    @Operation(summary = "Atualizar adotante")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AdotanteResponse> updateAdotante(@PathVariable UUID adotanteId, @RequestBody @Valid AdotanteRequest adotanteRequest) {
        AdotanteResponse adotanteResponse = iAdotanteService.updateAdotante(adotanteId, adotanteRequest);
        return ResponseEntity.ok(adotanteResponse);
    }

    @DeleteMapping("/{adotanteId}")
    @Operation(summary = "Excluir adotante")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteAdotante(@PathVariable UUID adotanteId) {
        iAdotanteService.deleteAdotante(adotanteId);
        return ResponseEntity.noContent().build();
    }
}
