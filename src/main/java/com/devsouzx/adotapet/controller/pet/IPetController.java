package com.devsouzx.adotapet.controller.pet;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetInfoResponse;
import com.devsouzx.adotapet.dto.response.PetResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPetController {
    ResponseEntity<List<PetInfoResponse>> getPets();
    ResponseEntity<PetResponse> createPet(Abrigo abrigo, PetRequest petRequest);
}
