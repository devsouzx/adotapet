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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        return toPetResponse(pet);
    }

    @Override
    public PetResponse getPetById(UUID identifier) {
        Pet pet = petRepository.findById(identifier).orElseThrow(() -> new RuntimeException("Pet not found"));

        return toPetResponse(pet);
    }

    @Override
    public PetResponse updatePet(UUID petId, PetRequest petRequest, Abrigo abrigo) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getAbrigo().getId().equals(abrigo.getId())) {
            throw new RuntimeException("You do not have permission to update this pet");
        }

        pet.setId(petId);
        pet.setNome(petRequest.nome());
        pet.setEspecie(petRequest.especie());
        pet.setRaca(petRequest.raca());
        pet.setDescricao(petRequest.descricao());
        pet.setIdadeEstimadaMeses(petRequest.idadeEstimadaMeses());
        pet.setPeso(petRequest.peso());
        pet.setFotoUrl(petRequest.fotoUrl());
        pet.setStatus(StatusPet.fromString(petRequest.status()));
        pet.setSexo(SexoPet.fromString(petRequest.sexo()));
        pet.setPorte(PortePet.fromString(petRequest.porte()));
        pet.setAbrigo(abrigo);

        petRepository.save(pet);
        return toPetResponse(pet);
    }

    @Override
    public PetResponse toPetResponse(Pet pet) {
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
    public void removePet(UUID petId, Abrigo abrigo) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getAbrigo().getId().equals(abrigo.getId())) {
            throw new RuntimeException("You do not have permission to remove this pet");
        }

        petRepository.delete(pet);
    }
}
