package com.devsouzx.adotapet.service.adotante.impl;

import com.devsouzx.adotapet.domain.adotante.Adotante;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import com.devsouzx.adotapet.repository.AdotanteRepository;
import com.devsouzx.adotapet.service.adotante.IAdotanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdotanteService implements IAdotanteService {
    private final AdotanteRepository adotanteRepository;

    @Override
    public Page<AdotanteResponse> getAdotantes(Integer page, Integer size) {
        Page<Adotante> adotantes = adotanteRepository.findAll(PageRequest.of(page, size));

        return adotantes.map(this::toResponse);
    }

    @Override
    public AdotanteResponse toResponse(Adotante adotante) {
        return AdotanteResponse.builder()
                .id(adotante.getId())
                .nome(adotante.getNome())
                .telefone(adotante.getTelefone())
                .email(adotante.getEmail())
                .dataNascimento(adotante.getDataNascimento())
                .build();
    }
}
