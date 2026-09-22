package com.devsouzx.adotapet.controller.pet.impl;

import com.devsouzx.adotapet.controller.pet.IPetController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetInfoResponse;
import com.devsouzx.adotapet.dto.response.PetResponse;
import com.devsouzx.adotapet.service.pet.IPetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet")
public class PetController implements IPetController {
    private final IPetService iPetService;

    @GetMapping
    public ResponseEntity<List<PetInfoResponse>> getPets() {
        List<PetInfoResponse> pets = iPetService.getPets();
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@AuthenticationPrincipal Abrigo abrigo, @RequestBody PetRequest petRequest) {
        PetResponse pet = iPetService.createPet(petRequest, abrigo);
        return ResponseEntity.ok(pet);
    }
}
