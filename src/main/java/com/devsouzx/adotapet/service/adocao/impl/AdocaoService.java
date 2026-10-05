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
import com.devsouzx.adotapet.dto.response.AdocaoResponse;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import com.devsouzx.adotapet.dto.response.PetAdocaoResponse;
import com.devsouzx.adotapet.exception.AdopterNotFoundException;
import com.devsouzx.adotapet.exception.AdoptionAlreadyClosedException;
import com.devsouzx.adotapet.exception.AdoptionNotFoundException;
import com.devsouzx.adotapet.exception.InvalidAdoptionDateException;
import com.devsouzx.adotapet.exception.InvalidClosureDateException;
import com.devsouzx.adotapet.exception.InvalidPaginationException;
import com.devsouzx.adotapet.exception.PetNotAvailableException;
import com.devsouzx.adotapet.exception.PetNotFoundException;
import com.devsouzx.adotapet.repository.AdocaoRepository;
import com.devsouzx.adotapet.repository.AdotanteRepository;
import com.devsouzx.adotapet.repository.PetRepository;
import com.devsouzx.adotapet.service.adocao.IAdocaoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdocaoService implements IAdocaoService {
    private final AdocaoRepository adocaoRepository;
    private final AdotanteRepository adotanteRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public Page<AdocaoResponse> listar(Abrigo abrigo, Integer page, Integer size) {
        validarPaginacao(page, size);
        return adocaoRepository.findAllByAbrigoIdOrderByDataAdocaoDesc(
                abrigo.getId(),
                PageRequest.of(page, size)
        ).map(this::toResponse);
    }

    @Override
    @Transactional
    public AdocaoResponse buscarPorId(Abrigo abrigo, UUID id) {
        return toResponse(buscarDoAbrigo(abrigo, id));
    }

    @Override
    @Transactional
    public AdocaoResponse registrar(Abrigo abrigo, AdocaoRequest request) {
        Pet pet = petRepository.findById(request.petId())
                .orElseThrow(PetNotFoundException::new);
        if (!pet.getAbrigo().getId().equals(abrigo.getId())) {
            throw new PetNotFoundException();
        }
        if (pet.getStatus() != StatusPet.DISPONIVEL
                || adocaoRepository.existsByPetIdAndStatus(request.petId(), StatusAdocao.ATIVA)) {
            throw new PetNotAvailableException();
        }

        Adotante adotante = buscarAdotante(request.adotanteId());
        LocalDate dataAdocao = request.dataAdocao() == null ? LocalDate.now() : request.dataAdocao();
        validarDataAdocao(dataAdocao);

        Adocao adocao = new Adocao();
        adocao.setDataAdocao(dataAdocao);
        adocao.setObservacoes(request.observacoes());
        adocao.setStatus(StatusAdocao.ATIVA);
        adocao.setAbrigo(abrigo);
        adocao.setPet(pet);
        adocao.setAdotante(adotante);

        pet.setStatus(StatusPet.ADOTADO);
        petRepository.save(pet);
        return toResponse(adocaoRepository.save(adocao));
    }

    @Override
    @Transactional
    public AdocaoResponse atualizar(Abrigo abrigo, UUID id, AdocaoUpdateRequest request) {
        Adocao adocao = buscarDoAbrigo(abrigo, id);
        validarDataAdocao(request.dataAdocao());
        if (adocao.getDataEncerramento() != null && request.dataAdocao().isAfter(adocao.getDataEncerramento())) {
            throw new InvalidAdoptionDateException("A data da adoção não pode ser posterior ao encerramento");
        }

        adocao.setAdotante(buscarAdotante(request.adotanteId()));
        adocao.setDataAdocao(request.dataAdocao());
        adocao.setObservacoes(request.observacoes());
        return toResponse(adocaoRepository.save(adocao));
    }

    @Override
    @Transactional
    public AdocaoResponse encerrar(Abrigo abrigo, UUID id, EncerramentoAdocaoRequest request) {
        Adocao adocao = buscarDoAbrigo(abrigo, id);
        if (adocao.getStatus() == StatusAdocao.ENCERRADA) {
            throw new AdoptionAlreadyClosedException();
        }

        LocalDate dataEncerramento = request == null || request.dataEncerramento() == null
                ? LocalDate.now()
                : request.dataEncerramento();
        if (dataEncerramento.isBefore(adocao.getDataAdocao()) || dataEncerramento.isAfter(LocalDate.now())) {
            throw new InvalidClosureDateException();
        }

        adocao.setStatus(StatusAdocao.ENCERRADA);
        adocao.setDataEncerramento(dataEncerramento);
        adocao.setMotivoEncerramento(request == null ? null : request.motivoEncerramento());
        if (adocao.getPet().getStatus() == StatusPet.ADOTADO) {
            adocao.getPet().setStatus(StatusPet.DISPONIVEL);
            petRepository.save(adocao.getPet());
        }

        return toResponse(adocaoRepository.save(adocao));
    }

    @Override
    @Transactional
    public void excluir(Abrigo abrigo, UUID id) {
        Adocao adocao = buscarDoAbrigo(abrigo, id);
        if (adocao.getStatus() == StatusAdocao.ATIVA
                && adocao.getPet().getStatus() == StatusPet.ADOTADO) {
            adocao.getPet().setStatus(StatusPet.DISPONIVEL);
            petRepository.save(adocao.getPet());
        }
        adocaoRepository.delete(adocao);
    }

    private Adocao buscarDoAbrigo(Abrigo abrigo, UUID id) {
        return adocaoRepository.findByIdAndAbrigoId(id, abrigo.getId())
                .orElseThrow(AdoptionNotFoundException::new);
    }

    private Adotante buscarAdotante(UUID id) {
        return adotanteRepository.findById(id)
                .orElseThrow(AdopterNotFoundException::new);
    }

    private void validarPaginacao(Integer page, Integer size) {
        if (page == null || page < 0 || size == null || size < 1 || size > 100) {
            throw new InvalidPaginationException();
        }
    }

    private void validarDataAdocao(LocalDate data) {
        if (data == null || data.isAfter(LocalDate.now())) {
            throw new InvalidAdoptionDateException();
        }
    }

    private AdocaoResponse toResponse(Adocao adocao) {
        Adotante adotante = adocao.getAdotante();
        return new AdocaoResponse(
                adocao.getId(),
                adocao.getDataAdocao(),
                adocao.getStatus(),
                adocao.getObservacoes(),
                adocao.getDataEncerramento(),
                adocao.getMotivoEncerramento(),
                adocao.getAbrigo().getId(),
                new PetAdocaoResponse(adocao.getPet().getId(), adocao.getPet().getNome()),
                AdotanteResponse.builder()
                        .id(adotante.getId())
                        .nome(adotante.getNome())
                        .telefone(adotante.getTelefone())
                        .email(adotante.getEmail())
                        .dataNascimento(adotante.getDataNascimento())
                        .build()
        );
    }
}
