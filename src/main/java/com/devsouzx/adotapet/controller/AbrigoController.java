package com.devsouzx.adotapet.controller;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.AbrigoInfoResponse;
import com.devsouzx.adotapet.service.AbrigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/abrigo")
@RequiredArgsConstructor
public class AbrigoController {
    private final AbrigoService abrigoService;

    @GetMapping
    public ResponseEntity<AbrigoInfoResponse> getAbrigoLoggedInfo(@AuthenticationPrincipal Abrigo abrigo) {
        return ResponseEntity.ok(abrigoService.getAbrigoInfoById(abrigo.getId()));
    }

    @GetMapping("/byidentifier/{identifier}")
    public ResponseEntity<AbrigoInfoResponse> getAbrigoById(@PathVariable("identifier") UUID identifier) {
        return ResponseEntity.ok(abrigoService.getAbrigoInfoById(identifier));
    }
}
