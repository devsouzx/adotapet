package com.devsouzx.adotapet.controller.adocao;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdocaoRequest;
import com.devsouzx.adotapet.dto.request.AdocaoUpdateRequest;
import com.devsouzx.adotapet.dto.request.EncerramentoAdocaoRequest;
import com.devsouzx.adotapet.dto.response.AdocaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IAdocaoController {
    ResponseEntity<Page<AdocaoResponse>> listar(Abrigo abrigo, Integer page, Integer size);
    ResponseEntity<AdocaoResponse> buscarPorId(Abrigo abrigo, UUID id);
    ResponseEntity<AdocaoResponse> registrar(Abrigo abrigo, AdocaoRequest request);
    ResponseEntity<AdocaoResponse> atualizar(Abrigo abrigo, UUID id, AdocaoUpdateRequest request);
    ResponseEntity<AdocaoResponse> encerrar(Abrigo abrigo, UUID id, EncerramentoAdocaoRequest request);
    ResponseEntity<Void> excluir(Abrigo abrigo, UUID id);
}
