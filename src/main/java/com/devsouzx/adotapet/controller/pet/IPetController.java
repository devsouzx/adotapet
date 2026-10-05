package com.devsouzx.adotapet.controller.pet;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetInfoResponse;
import com.devsouzx.adotapet.dto.response.PetResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IPetController {
    ResponseEntity<List<PetInfoResponse>> getPets();
    ResponseEntity<PetResponse> createPet(Abrigo abrigo, PetRequest petRequest);
    ResponseEntity<PetResponse> getPetById(UUID id);
    ResponseEntity<PetResponse> updatePet(Abrigo abrigo, UUID petId, PetRequest petRequest);
    ResponseEntity<Void> deletePet(Abrigo abrigo, UUID petId);
    ResponseEntity<Page<PetResponse>> getPetByFiltros(
            String nome,
            String especie,
            String raca,
            Integer idadeEstimadaMeses,
            BigDecimal peso,
            String status,
            String sexo,
            String porte,
            Integer page,
            Integer size
    );
}
