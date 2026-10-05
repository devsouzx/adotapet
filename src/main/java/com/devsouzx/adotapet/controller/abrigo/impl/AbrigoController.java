package com.devsouzx.adotapet.controller.abrigo.impl;

import com.devsouzx.adotapet.controller.abrigo.IAbrigoController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.response.AbrigoInfoResponse;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
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
@RequestMapping("/abrigo")
@RequiredArgsConstructor
@Tag(name = "Abrigos", description = "Consulta e gerenciamento de abrigos.")
public class AbrigoController implements IAbrigoController {
    private final IAbrigoService iAbrigoService;

    @GetMapping
    @Operation(summary = "Consultar perfil do abrigo autenticado")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AbrigoInfoResponse> getAbrigoLoggedInfo(@AuthenticationPrincipal Abrigo abrigo) {
        return ResponseEntity.ok(iAbrigoService.getAbrigoInfoById(abrigo.getId()));
    }

    @GetMapping("/{identifier}")
    @Operation(summary = "Consultar abrigo por identificador")
    public ResponseEntity<AbrigoInfoResponse> getAbrigoById(@PathVariable("identifier") UUID abrigoIdentifier) {
        return ResponseEntity.ok(iAbrigoService.getAbrigoInfoById(abrigoIdentifier));
    }

    @PutMapping("/editar")
    @Operation(summary = "Atualizar dados do abrigo autenticado")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AbrigoInfoResponse> updateAbrigoInfo(@AuthenticationPrincipal Abrigo abrigo, @RequestBody @Valid AbrigoUpdateRequest abrigoUpdateRequest) {
            return ResponseEntity.ok(iAbrigoService.updateAbrigo(abrigo.getId(), abrigoUpdateRequest));
    }

    @GetMapping("/proximos")
    @Operation(summary = "Buscar abrigos próximos de uma localização")
    public ResponseEntity<Page<AbrigoInfoResponse>> getAbrigosProximos(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam(name = "raio", defaultValue = "10.0") double raio,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Page<AbrigoInfoResponse> abrigosProximos = iAbrigoService.getAbrigosProximos(latitude, longitude, raio, page, size);
        return ResponseEntity.ok(abrigosProximos);
    }
}
