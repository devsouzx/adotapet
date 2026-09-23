package com.devsouzx.adotapet.controller.pet.impl;

import com.devsouzx.adotapet.controller.pet.IPetController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetInfoResponse;
import com.devsouzx.adotapet.dto.response.PetResponse;
import com.devsouzx.adotapet.service.pet.IPetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/novo")
    public ResponseEntity<PetResponse> createPet(@AuthenticationPrincipal Abrigo abrigo, @RequestBody PetRequest petRequest) {
        PetResponse pet = iPetService.createPet(petRequest, abrigo);
        return ResponseEntity.ok(pet);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable UUID identifier) {
        PetResponse pet = iPetService.getPetById(identifier);
        return ResponseEntity.ok(pet);
    }

    @PutMapping("/{identifier}/editar")
    public ResponseEntity<PetResponse> updatePet(@AuthenticationPrincipal Abrigo abrigo, @PathVariable("identifier") UUID petId, @RequestBody PetRequest petRequest) {
        PetResponse pet = iPetService.updatePet(petId, petRequest, abrigo);
        return ResponseEntity.ok(pet);
    }

    @DeleteMapping("/{identifier}/remover")
    public ResponseEntity<Void> deletePet(@AuthenticationPrincipal Abrigo abrigo, @PathVariable("identifier") UUID petId) {
        iPetService.removePet(petId, abrigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtros")
    public ResponseEntity<Page<PetResponse>> getPetByFiltros(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "especie", required = false) String especie,
            @RequestParam(name = "raca", required = false) String raca,
            @RequestParam(name = "idadeEstimadaMeses", required = false) Integer idadeEstimadaMeses,
            @RequestParam(name = "peso", required = false) BigDecimal peso,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sexo", required = false) String sexo,
            @RequestParam(name = "porte", required = false) String porte,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Page<PetResponse> pet = iPetService.getPetByFiltros(nome, especie, raca, idadeEstimadaMeses, peso, status, sexo, porte, page, size);
        return ResponseEntity.ok(pet);
    }
}
