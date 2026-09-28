package com.devsouzx.adotapet.controller.adocao.impl;

import com.devsouzx.adotapet.controller.adocao.IAdocaoController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdocaoRequest;
import com.devsouzx.adotapet.dto.request.AdocaoUpdateRequest;
import com.devsouzx.adotapet.dto.request.EncerramentoAdocaoRequest;
import com.devsouzx.adotapet.dto.response.AdocaoResponse;
import com.devsouzx.adotapet.service.adocao.IAdocaoService;
import jakarta.validation.Valid;
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
public class AdocaoController implements IAdocaoController {
    private final IAdocaoService adocaoService;

    @Override
    @GetMapping
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
    public ResponseEntity<AdocaoResponse> buscarPorId(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id
    ) {
        AdocaoResponse adocao = adocaoService.buscarPorId(abrigo, id);
        return ResponseEntity.ok(adocao);
    }

    @Override
    @PostMapping
    public ResponseEntity<AdocaoResponse> registrar(
            @AuthenticationPrincipal Abrigo abrigo,
            @RequestBody @Valid AdocaoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adocaoService.registrar(abrigo, request));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<AdocaoResponse> atualizar(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id,
            @RequestBody @Valid AdocaoUpdateRequest request
    ) {
        return ResponseEntity.ok(adocaoService.atualizar(abrigo, id, request));
    }

    @Override
    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<AdocaoResponse> encerrar(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id,
            @RequestBody(required = false) EncerramentoAdocaoRequest request
    ) {
        return ResponseEntity.ok(adocaoService.encerrar(abrigo, id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @AuthenticationPrincipal Abrigo abrigo,
            @PathVariable UUID id
    ) {
        adocaoService.excluir(abrigo, id);
        return ResponseEntity.noContent().build();
    }
}
