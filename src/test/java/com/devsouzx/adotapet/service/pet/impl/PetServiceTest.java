package com.devsouzx.adotapet.service.pet.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.pet.Pet;
import com.devsouzx.adotapet.domain.pet.PortePet;
import com.devsouzx.adotapet.domain.pet.SexoPet;
import com.devsouzx.adotapet.domain.pet.StatusPet;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.dto.response.PetResponse;
import com.devsouzx.adotapet.exception.InvalidPaginationException;
import com.devsouzx.adotapet.exception.PetDeletionForbiddenException;
import com.devsouzx.adotapet.exception.PetNotFoundException;
import com.devsouzx.adotapet.exception.PetUpdateForbiddenException;
import com.devsouzx.adotapet.repository.PetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceTest {
    @Mock private PetRepository petRepository;
    @InjectMocks private PetService service;

    private UUID abrigoId;
    private Abrigo abrigo;

    @Before
    public void setUp() {
        abrigoId = UUID.randomUUID();
        abrigo = Abrigo.builder().id(abrigoId).nome("Abrigo Central").build();
    }

    @Test
    public void cadastraPetEConverteEnumeracoesEAbrigo() {
        PetRequest request = request("Luna", "disponivel");
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        PetResponse response = service.createPet(request, abrigo);

        assertEquals("Luna", response.nome());
        assertEquals(StatusPet.DISPONIVEL, response.status());
        assertEquals(SexoPet.FEMEA, response.sexo());
        assertEquals(PortePet.PEQUENO, response.porte());
        assertEquals(abrigoId, response.abrigo().id());
        assertNotNull(response.dataCadastro());
        verify(petRepository).save(argThat(pet -> pet.getAbrigo() == abrigo
                && pet.getDataCadastro() != null && pet.getPeso().compareTo(new BigDecimal("4.2")) == 0));
    }

    @Test
    public void buscaPetEInformaExcecaoQuandoNaoExiste() {
        UUID id = UUID.randomUUID();
        Pet pet = pet(id, abrigo, StatusPet.DISPONIVEL);
        when(petRepository.findById(id)).thenReturn(Optional.of(pet));
        assertEquals("Luna", service.getPetById(id).nome());

        when(petRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(PetNotFoundException.class, () -> service.getPetById(id));
    }

    @Test
    public void atualizaSomentePetDoAbrigoAutenticado() {
        UUID id = UUID.randomUUID();
        Pet pet = pet(id, abrigo, StatusPet.DISPONIVEL);
        when(petRepository.findById(id)).thenReturn(Optional.of(pet));

        PetResponse result = service.updatePet(id, request("Nina", "adotado"), abrigo);
        assertEquals("Nina", result.nome());
        assertEquals(StatusPet.ADOTADO, result.status());
        verify(petRepository).save(pet);

        Abrigo other = Abrigo.builder().id(UUID.randomUUID()).nome("Outro").build();
        reset(petRepository);
        when(petRepository.findById(id)).thenReturn(Optional.of(pet));
        assertThrows(PetUpdateForbiddenException.class, () -> service.updatePet(id, request("X", "disponivel"), other));
        verify(petRepository, never()).save(any());
    }

    @Test
    public void removePetSomenteQuandoPertenceAoAbrigo() {
        UUID id = UUID.randomUUID();
        Pet pet = pet(id, abrigo, StatusPet.DISPONIVEL);
        when(petRepository.findById(id)).thenReturn(Optional.of(pet));

        service.removePet(id, abrigo);
        verify(petRepository).delete(pet);

        when(petRepository.findById(id)).thenReturn(Optional.of(pet));
        assertThrows(PetDeletionForbiddenException.class,
                () -> service.removePet(id, Abrigo.builder().id(UUID.randomUUID()).build()));
        when(petRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(PetNotFoundException.class, () -> service.removePet(id, abrigo));
    }

    @Test
    public void filtraEnumeracoesSemEspacosEUsaPaginaSolicitada() {
        when(petRepository.findByFiltros(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(pet(UUID.randomUUID(), abrigo, StatusPet.DISPONIVEL))));

        assertEquals(1, service.getPetByFiltros(null, null, null, null, null,
                " DISPONIVEL ", " FEMEA ", " PEQUENO ", 2, 5).getTotalElements());
        verify(petRepository).findByFiltros(isNull(), isNull(), isNull(), isNull(), isNull(),
                eq("DISPONIVEL"), eq("FEMEA"), eq("PEQUENO"), eq(PageRequest.of(2, 5)));
    }

    @Test
    public void rejeitaPaginacaoInvalidaNosFiltros() {
        assertThrows(InvalidPaginationException.class, () -> service.getPetByFiltros(null,null,null,null,null,null,null,null,null,5));
        assertThrows(InvalidPaginationException.class, () -> service.getPetByFiltros(null,null,null,null,null,null,null,null,-1,5));
        assertThrows(InvalidPaginationException.class, () -> service.getPetByFiltros(null,null,null,null,null,null,null,null,0,0));
        assertThrows(InvalidPaginationException.class, () -> service.getPetByFiltros(null,null,null,null,null,null,null,null,0,101));
        verifyNoInteractions(petRepository);
    }

    private static PetRequest request(String name, String status) {
        return new PetRequest(name, "Cachorro", "SRD", "Amigável", 12, new BigDecimal("4.2"),
                null, status, "FEMEA", "PEQUENO");
    }

    private static Pet pet(UUID id, Abrigo abrigo, StatusPet status) {
        return Pet.builder().id(id).nome("Luna").especie("Cachorro").raca("SRD")
                .descricao("Amigável").idadeEstimadaMeses(12).peso(new BigDecimal("4.2"))
                .dataCadastro(LocalDateTime.of(2025, 1, 2, 3, 4)).status(status)
                .sexo(SexoPet.FEMEA).porte(PortePet.PEQUENO).abrigo(abrigo).build();
    }
}
