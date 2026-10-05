package com.devsouzx.adotapet.controller.adocao.impl;

import com.devsouzx.adotapet.controller.adocao.IAdocaoController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdocaoRequest;
import com.devsouzx.adotapet.dto.request.AdocaoUpdateRequest;
import com.devsouzx.adotapet.dto.request.EncerramentoAdocaoRequest;
import com.devsouzx.adotapet.dto.response.AdocaoResponse;
import com.devsouzx.adotapet.service.adocao.IAdocaoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/adocao")
@RequiredArgsConstructor
@Tag(name = "Adoções", description = "Registro, consulta e gerenciamento de adoções.")
@SecurityRequirement(name = "bearerAuth")
public class AdocaoController implements IAdocaoController {
    private final IAdocaoService adocaoService;

    @Override
    @GetMapping
    @Operation(summary = "Listar adoções do abrigo autenticado")
    public ResponseEntity<Page<AdocaoResponse>> listar(
            @AuthenticationPrincipal Abrigo abrigo,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<AdocaoResponse> adocoes = adocaoService.listar(abrigo, page, size);
        return ResponseEntity.ok(adocoes);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Consultar adoção por identificador")
    public ResponseEntity<AdocaoResponse> buscarPorId(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id
    ) {
        AdocaoResponse adocao = adocaoService.buscarPorId(abrigo, id);
        return ResponseEntity.ok(adocao);
    }

    @Override
    @PostMapping
    @Operation(summary = "Registrar adoção")
    public ResponseEntity<AdocaoResponse> registrar(
            @AuthenticationPrincipal Abrigo abrigo,
            @RequestBody @Valid AdocaoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adocaoService.registrar(abrigo, request));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar adoção")
    public ResponseEntity<AdocaoResponse> atualizar(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id,
            @RequestBody @Valid AdocaoUpdateRequest request
    ) {
        return ResponseEntity.ok(adocaoService.atualizar(abrigo, id, request));
    }

    @Override
    @PatchMapping("/{id}/encerrar")
    @Operation(summary = "Encerrar adoção")
    public ResponseEntity<AdocaoResponse> encerrar(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id,
            @RequestBody(required = false) EncerramentoAdocaoRequest request
    ) {
        return ResponseEntity.ok(adocaoService.encerrar(abrigo, id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir adoção")
    public ResponseEntity<Void> excluir(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id
    ) {
        adocaoService.excluir(abrigo, id);
        return ResponseEntity.noContent().build();
    }
}
