package com.devsouzx.adotapet.service.pet.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.pet.Pet;
import com.devsouzx.adotapet.domain.pet.PortePet;
import com.devsouzx.adotapet.domain.pet.SexoPet;
import com.devsouzx.adotapet.domain.pet.StatusPet;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetInfoResponse;
import com.devsouzx.adotapet.dto.response.PetResponse;
import com.devsouzx.adotapet.repository.PetRepository;
import com.devsouzx.adotapet.service.pet.IPetService;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService implements IPetService {
    private final PetRepository petRepository;

    @Override
    public List<PetInfoResponse> getPets() {
        List<Pet> pets = petRepository.findAll();

        return pets.stream()
                .map(pet -> new PetInfoResponse(
                        pet.getNome(),
                        pet.getEspecie(),
                        pet.getRaca(),
                        pet.getDescricao(),
                        pet.getIdadeEstimadaMeses(),
                        pet.getPeso(),
                        pet.getFotoUrl(),
                        pet.getDataCadastro(),
                        pet.getStatus(),
                        pet.getSexo(),
                        pet.getPorte(),
                        pet.getAbrigo()
                ))
                .toList();
    }

    @Override
    public PetResponse createPet(PetRequest petRequest, Abrigo abrigo) {
        Pet pet = Pet.builder()
                .nome(petRequest.nome())
                .especie(petRequest.especie())
                .raca(petRequest.raca())
                .descricao(petRequest.descricao())
                .idadeEstimadaMeses(petRequest.idadeEstimadaMeses())
                .peso(petRequest.peso())
                .fotoUrl(petRequest.fotoUrl())
                .dataCadastro(LocalDateTime.now())
                .status(StatusPet.fromString(petRequest.status()))
                .sexo(SexoPet.fromString(petRequest.sexo()))
                .porte(PortePet.fromString(petRequest.porte()))
                .abrigo(abrigo)
                .build();

        petRepository.save(pet);
        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getEspecie(),
                pet.getRaca(),
                pet.getDescricao(),
                pet.getIdadeEstimadaMeses(),
                pet.getPeso(),
                pet.getFotoUrl(),
                pet.getDataCadastro(),
                pet.getStatus(),
                pet.getSexo(),
                pet.getPorte(),
                pet.getAbrigo()
        );
    }

    @Override
    public PetResponse getPetById(UUID identifier) {
        Pet pet = petRepository.findById(identifier).orElseThrow(() -> new RuntimeException("Pet not found"));
        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getEspecie(),
                pet.getRaca(),
                pet.getDescricao(),
                pet.getIdadeEstimadaMeses(),
                pet.getPeso(),
                pet.getFotoUrl(),
                pet.getDataCadastro(),
                pet.getStatus(),
                pet.getSexo(),
                pet.getPorte(),
                pet.getAbrigo()
        );
    }
}
