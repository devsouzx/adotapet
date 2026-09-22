package com.devsouzx.adotapet.controller.abrigo;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.response.AbrigoInfoResponse;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IAbrigoController {
    ResponseEntity<AbrigoInfoResponse> getAbrigoLoggedInfo(Abrigo abrigo) throws Exception;
    ResponseEntity<AbrigoInfoResponse> getAbrigoById(UUID abrigoIdentifier) throws Exception;
    ResponseEntity<AbrigoInfoResponse> updateAbrigoInfo(Abrigo abrigo, AbrigoUpdateRequest abrigoUpdateRequest) throws Exception;
}
