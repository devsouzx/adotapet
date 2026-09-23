package com.devsouzx.adotapet.service.pet;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.pet.Pet;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetInfoResponse;
import com.devsouzx.adotapet.dto.response.PetResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IPetService {
    List<PetInfoResponse> getPets();;
    PetResponse createPet(PetRequest petRequest, Abrigo abrigo);
    PetResponse getPetById(UUID identifier);
    PetResponse updatePet(UUID petId, PetRequest petRequest, Abrigo abrigo);
    PetResponse toPetResponse(Pet pet);
    void removePet(UUID petId, Abrigo abrigo);
    Page<PetResponse> getPetByFiltros(String nome, String especie, String raca, Integer idadeEstimadaMeses, BigDecimal peso, String status, String sexo, String porte, Integer page, Integer size);
}
