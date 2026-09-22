package com.devsouzx.adotapet.controller.abrigo.impl;

import com.devsouzx.adotapet.controller.abrigo.IAbrigoController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.response.AbrigoInfoResponse;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/abrigo")
@RequiredArgsConstructor
public class AbrigoController implements IAbrigoController {
    private final IAbrigoService iAbrigoService;

    @GetMapping
    public ResponseEntity<AbrigoInfoResponse> getAbrigoLoggedInfo(@AuthenticationPrincipal Abrigo abrigo) throws Exception {
        return ResponseEntity.ok(iAbrigoService.getAbrigoInfoById(abrigo.getId()));
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<AbrigoInfoResponse> getAbrigoById(@PathVariable("identifier") UUID abrigoIdentifier) throws Exception {
        return ResponseEntity.ok(iAbrigoService.getAbrigoInfoById(abrigoIdentifier));
    }

    @PutMapping("/editar")
    public ResponseEntity<AbrigoInfoResponse> updateAbrigoInfo(@AuthenticationPrincipal Abrigo abrigo, @RequestBody AbrigoUpdateRequest abrigoUpdateRequest) throws Exception {
            return ResponseEntity.ok(iAbrigoService.updateAbrigoInfo(abrigo.getId(), abrigoUpdateRequest));
    }

    @GetMapping("/proximos")
    public ResponseEntity<List<AbrigoInfoResponse>> getAbrigosProximos(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, @RequestParam(name = "raio", defaultValue = "10.0") double raio) {
        List<AbrigoInfoResponse> abrigosProximos = iAbrigoService.getAbrigosProximos(latitude, longitude, raio);
        return ResponseEntity.ok(abrigosProximos);
    }
}
