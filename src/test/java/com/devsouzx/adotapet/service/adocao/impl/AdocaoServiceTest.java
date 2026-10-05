package com.devsouzx.adotapet.service.adocao.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.adocao.Adocao;
import com.devsouzx.adotapet.domain.adocao.StatusAdocao;
import com.devsouzx.adotapet.domain.adotante.Adotante;
import com.devsouzx.adotapet.domain.pet.Pet;
import com.devsouzx.adotapet.domain.pet.StatusPet;
import com.devsouzx.adotapet.dto.request.AdocaoRequest;
import com.devsouzx.adotapet.dto.request.AdocaoUpdateRequest;
import com.devsouzx.adotapet.dto.request.EncerramentoAdocaoRequest;
import com.devsouzx.adotapet.exception.*;
import com.devsouzx.adotapet.repository.AdocaoRepository;
import com.devsouzx.adotapet.repository.AdotanteRepository;
import com.devsouzx.adotapet.repository.PetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdocaoServiceTest {
    @Mock private AdocaoRepository adocaoRepository;
    @Mock private AdotanteRepository adotanteRepository;
    @Mock private PetRepository petRepository;
    @InjectMocks private AdocaoService service;

    private Abrigo shelter;
    private Adotante adopter;
    private Pet pet;

    @Before
    public void setUp() {
        shelter = Abrigo.builder().id(UUID.randomUUID()).nome("Abrigo").build();
        adopter = Adotante.builder().id(UUID.randomUUID()).nome("Joana").email("j@example.com").build();
        pet = Pet.builder().id(UUID.randomUUID()).nome("Tobi").abrigo(shelter).status(StatusPet.DISPONIVEL).build();
    }

    @Test
    public void listaAdocoesDoAbrigoComPaginacaoEValidaLimites() {
        Adocao adoption = adoption(UUID.randomUUID(), StatusAdocao.ATIVA, LocalDate.now());
        when(adocaoRepository.findAllByAbrigoIdOrderByDataAdocaoDesc(eq(shelter.getId()), eq(PageRequest.of(1, 5))))
                .thenReturn(new PageImpl<>(List.of(adoption)));
        assertEquals(1, service.listar(shelter, 1, 5).getTotalElements());
        assertEquals("Tobi", service.listar(shelter, 1, 5).getContent().get(0).pet().nome());
        assertThrows(InvalidPaginationException.class, () -> service.listar(shelter, null, 5));
        assertThrows(InvalidPaginationException.class, () -> service.listar(shelter, 0, 101));
    }

    @Test
    public void registraAdocaoEAtualizaSituacaoDoPet() {
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(adotanteRepository.findById(adopter.getId())).thenReturn(Optional.of(adopter));
        when(adocaoRepository.existsByPetIdAndStatus(pet.getId(), StatusAdocao.ATIVA)).thenReturn(false);
        when(adocaoRepository.save(any(Adocao.class))).thenAnswer(invocation -> {
            Adocao saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        var response = service.registrar(shelter,
                new AdocaoRequest(pet.getId(), adopter.getId(), LocalDate.now(), "Observação"));
        assertEquals(StatusAdocao.ATIVA, response.status());
        assertEquals("Joana", response.adotante().nome());
        assertEquals("Tobi", response.pet().nome());
        assertEquals(StatusPet.ADOTADO, pet.getStatus());
        verify(petRepository).save(pet);
        verify(adocaoRepository).save(argThat(a -> a.getAbrigo() == shelter
                && a.getAdotante() == adopter && "Observação".equals(a.getObservacoes())));
    }

    @Test
    public void rejeitaPetIndisponivelOuDeOutroAbrigo() {
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        pet.setStatus(StatusPet.ADOTADO);
        assertThrows(PetNotAvailableException.class, () -> service.registrar(shelter,
                new AdocaoRequest(pet.getId(), adopter.getId(), LocalDate.now(), null)));
        verifyNoInteractions(adotanteRepository);

        pet.setStatus(StatusPet.DISPONIVEL);
        pet.setAbrigo(Abrigo.builder().id(UUID.randomUUID()).build());
        assertThrows(PetNotFoundException.class, () -> service.registrar(shelter,
                new AdocaoRequest(pet.getId(), adopter.getId(), LocalDate.now(), null)));
    }

    @Test
    public void rejeitaAdocaoAtivaDuplicadaEDataFutura() {
        pet.setStatus(StatusPet.DISPONIVEL);
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(adocaoRepository.existsByPetIdAndStatus(pet.getId(), StatusAdocao.ATIVA)).thenReturn(true);
        assertThrows(PetNotAvailableException.class, () -> service.registrar(shelter,
                new AdocaoRequest(pet.getId(), adopter.getId(), LocalDate.now(), null)));

        when(adocaoRepository.existsByPetIdAndStatus(pet.getId(), StatusAdocao.ATIVA)).thenReturn(false);
        when(adotanteRepository.findById(adopter.getId())).thenReturn(Optional.of(adopter));
        assertThrows(InvalidAdoptionDateException.class, () -> service.registrar(shelter,
                new AdocaoRequest(pet.getId(), adopter.getId(), LocalDate.now().plusDays(1), null)));
    }

    @Test
    public void informaAdotanteOuAdocaoInexistenteEValidaPropriedadeDoAbrigo() {
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        when(adocaoRepository.existsByPetIdAndStatus(any(), any())).thenReturn(false);
        when(adotanteRepository.findById(adopter.getId())).thenReturn(Optional.empty());
        assertThrows(AdopterNotFoundException.class, () -> service.registrar(shelter,
                new AdocaoRequest(pet.getId(), adopter.getId(), LocalDate.now(), null)));

        UUID adoptionId = UUID.randomUUID();
        when(adocaoRepository.findByIdAndAbrigoId(adoptionId, shelter.getId())).thenReturn(Optional.empty());
        assertThrows(AdoptionNotFoundException.class, () -> service.buscarPorId(shelter, adoptionId));
    }

    @Test
    public void atualizaAdocaoERejeitaDataPosteriorAoEncerramento() {
        Adocao adoption = adoption(UUID.randomUUID(), StatusAdocao.ENCERRADA, LocalDate.of(2024, 1, 1));
        adoption.setDataEncerramento(LocalDate.of(2024, 2, 1));
        when(adocaoRepository.findByIdAndAbrigoId(adoption.getId(), shelter.getId())).thenReturn(Optional.of(adoption));
        when(adotanteRepository.findById(adopter.getId())).thenReturn(Optional.of(adopter));
        when(adocaoRepository.save(adoption)).thenReturn(adoption);
        var response = service.atualizar(shelter, adoption.getId(),
                new AdocaoUpdateRequest(adopter.getId(), LocalDate.of(2024, 1, 20), "Updated"));
        assertEquals("Updated", response.observacoes());
        assertEquals(LocalDate.of(2024, 1, 20), adoption.getDataAdocao());
        assertThrows(InvalidAdoptionDateException.class, () -> service.atualizar(shelter, adoption.getId(),
                new AdocaoUpdateRequest(adopter.getId(), LocalDate.of(2024, 2, 2), "Too late")));
    }

    @Test
    public void encerraAdocaoRestauraDisponibilidadeERejeitaEncerramentoInvalidoOuRepetido() {
        pet.setStatus(StatusPet.ADOTADO);
        Adocao adoption = adoption(UUID.randomUUID(), StatusAdocao.ATIVA, LocalDate.now().minusDays(2));
        when(adocaoRepository.findByIdAndAbrigoId(adoption.getId(), shelter.getId())).thenReturn(Optional.of(adoption));
        when(adocaoRepository.save(adoption)).thenReturn(adoption);

        var response = service.encerrar(shelter, adoption.getId(),
                new EncerramentoAdocaoRequest(LocalDate.now(), "Concluída"));
        assertEquals(StatusAdocao.ENCERRADA, response.status());
        assertEquals("Concluída", response.motivoEncerramento());
        assertEquals(StatusPet.DISPONIVEL, pet.getStatus());
        verify(petRepository).save(pet);
        assertThrows(AdoptionAlreadyClosedException.class, () -> service.encerrar(shelter, adoption.getId(), null));

        Adocao invalid = adoption(UUID.randomUUID(), StatusAdocao.ATIVA, LocalDate.now());
        when(adocaoRepository.findByIdAndAbrigoId(invalid.getId(), shelter.getId())).thenReturn(Optional.of(invalid));
        assertThrows(InvalidClosureDateException.class, () -> service.encerrar(shelter, invalid.getId(),
                new EncerramentoAdocaoRequest(LocalDate.now().plusDays(1), null)));
    }

    @Test
    public void excluirAdocaoAtivaTornaPetDisponivel() {
        pet.setStatus(StatusPet.ADOTADO);
        Adocao adoption = adoption(UUID.randomUUID(), StatusAdocao.ATIVA, LocalDate.now());
        when(adocaoRepository.findByIdAndAbrigoId(adoption.getId(), shelter.getId())).thenReturn(Optional.of(adoption));
        service.excluir(shelter, adoption.getId());
        assertEquals(StatusPet.DISPONIVEL, pet.getStatus());
        verify(petRepository).save(pet);
        verify(adocaoRepository).delete(adoption);
    }

    private Adocao adoption(UUID id, StatusAdocao status, LocalDate date) {
        Adocao adoption = new Adocao();
        adoption.setId(id);
        adoption.setStatus(status);
        adoption.setDataAdocao(date);
        adoption.setAbrigo(shelter);
        adoption.setAdotante(adopter);
        adoption.setPet(pet);
        return adoption;
    }
}
