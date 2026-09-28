package com.devsouzx.adotapet.service.adocao;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdocaoRequest;
import com.devsouzx.adotapet.dto.request.AdocaoUpdateRequest;
import com.devsouzx.adotapet.dto.request.EncerramentoAdocaoRequest;
import com.devsouzx.adotapet.dto.response.AdocaoResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IAdocaoService {
    Page<AdocaoResponse> listar(Abrigo abrigo, Integer page, Integer size);
    AdocaoResponse buscarPorId(Abrigo abrigo, UUID id);
    AdocaoResponse registrar(Abrigo abrigo, AdocaoRequest request);
    AdocaoResponse atualizar(Abrigo abrigo, UUID id, AdocaoUpdateRequest request);
    AdocaoResponse encerrar(Abrigo abrigo, UUID id, EncerramentoAdocaoRequest request);
    void excluir(Abrigo abrigo, UUID id);
}
