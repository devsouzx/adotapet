package com.devsouzx.adotapet.service.adotante;

import com.devsouzx.adotapet.domain.adotante.Adotante;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import org.springframework.data.domain.Page;

public interface IAdotanteService {
    Page<AdotanteResponse> getAdotantes(Integer page, Integer size);
    AdotanteResponse toResponse(Adotante adotante);
}
