package com.devsouzx.adotapet.service.adotante.impl;

import com.devsouzx.adotapet.domain.adotante.Adotante;
import com.devsouzx.adotapet.dto.request.AdotanteRequest;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import com.devsouzx.adotapet.repository.AdotanteRepository;
import com.devsouzx.adotapet.service.adotante.IAdotanteService;
import com.devsouzx.adotapet.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AdotanteService implements IAdotanteService {
    private final AdotanteRepository adotanteRepository;
    private final RedisService redisService;

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

    @Override
    public AdotanteResponse createAdotante(AdotanteRequest adotanteRequest) {
        Adotante adotante = Adotante.builder()
                .nome(adotanteRequest.nome())
                .telefone(adotanteRequest.telefone())
                .email(adotanteRequest.email())
                .dataNascimento(adotanteRequest.dataNascimento())
                .build();

        adotante = adotanteRepository.save(adotante);
        AdotanteResponse adotanteResponse = toResponse(adotante);
        redisService.setValue("ADOTANTE_" + adotante.getId(), adotanteResponse, TimeUnit.MILLISECONDS, 600000L);

        return adotanteResponse;
    }

    @Override
    public AdotanteResponse getAdotanteById(UUID adotanteId) {
        AdotanteResponse adotanteResponse = (AdotanteResponse) redisService.getValue("ADOTANTE_" + adotanteId, AdotanteResponse.class);
        if (adotanteResponse == null) {
            Adotante adotante = findById(adotanteId);;
            adotanteResponse = toResponse(adotante);
            redisService.setValue("ADOTANTE_" + adotante.getId(), adotanteResponse, TimeUnit.MILLISECONDS, 600000L);
        }

        return adotanteResponse;
    }

    @Override
    public AdotanteResponse updateAdotante(UUID adotanteId, AdotanteRequest adotanteRequest) {
        Adotante adotante = findById(adotanteId);;

        adotante.setId(adotante.getId());
        adotante.setNome(adotanteRequest.nome());
        adotante.setTelefone(adotanteRequest.telefone());
        adotante.setEmail(adotanteRequest.email());
        adotante.setDataNascimento(adotanteRequest.dataNascimento());

        adotante = adotanteRepository.save(adotante);

        return toResponse(adotante);
    }

    @Override
    public void deleteAdotante(UUID adotanteId) {
        Adotante adotante = findById(adotanteId);
        adotanteRepository.delete(adotante);
    }

    private Adotante findById(UUID adotanteId) {
        return adotanteRepository.findById(adotanteId).orElseThrow(() -> new RuntimeException("Adotante não encontrado"));
    }
}
