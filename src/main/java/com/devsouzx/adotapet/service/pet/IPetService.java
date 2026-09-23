package com.devsouzx.adotapet.service.pet;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.pet.Pet;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetInfoResponse;
import com.devsouzx.adotapet.dto.response.PetResponse;

import java.util.List;
import java.util.UUID;

public interface IPetService {
    List<PetInfoResponse> getPets();;
    PetResponse createPet(PetRequest petRequest, Abrigo abrigo);
    PetResponse getPetById(UUID identifier);
    PetResponse updatePet(UUID petId, PetRequest petRequest, Abrigo abrigo);
    PetResponse toPetResponse(Pet pet);
}
