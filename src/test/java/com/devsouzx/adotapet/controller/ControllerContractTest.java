package com.devsouzx.adotapet.controller;

import com.devsouzx.adotapet.controller.adocao.impl.AdocaoController;
import com.devsouzx.adotapet.controller.adotante.impl.AdotanteController;
import com.devsouzx.adotapet.controller.authentication.impl.AuthenticationController;
import com.devsouzx.adotapet.controller.pet.impl.PetController;
import com.devsouzx.adotapet.controller.abrigo.impl.AbrigoController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.AdocaoRequest;
import com.devsouzx.adotapet.dto.request.UserRequestResetPasswordRequest;
import com.devsouzx.adotapet.dto.request.UserResetPasswordRequest;
import com.devsouzx.adotapet.dto.response.AdocaoResponse;
import com.devsouzx.adotapet.service.adotante.IAdotanteService;
import com.devsouzx.adotapet.service.adocao.IAdocaoService;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
import com.devsouzx.adotapet.service.authentication.IAuthenticationService;
import com.devsouzx.adotapet.infra.config.TokenService;
import com.devsouzx.adotapet.service.pet.IPetService;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ControllerContractTest {
    @Test
    public void controllerDeAdocaoUsaRotasERetornaStatusEsperadosAoCriarEExcluir() throws Exception {
        IAdocaoService service = mock(IAdocaoService.class);
        AdocaoController controller = new AdocaoController(service);
        Abrigo shelter = Abrigo.builder().id(UUID.randomUUID()).build();
        UUID petId = UUID.randomUUID();
        UUID adopterId = UUID.randomUUID();
        AdocaoResponse response = new AdocaoResponse(UUID.randomUUID(), null, null, null, null, null,
                shelter.getId(), null, null);
        when(service.registrar(eq(shelter), any(AdocaoRequest.class))).thenReturn(response);

        ResponseEntity<AdocaoResponse> created = controller.registrar(shelter,
                new AdocaoRequest(petId, adopterId, null, null));
        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertSame(response, created.getBody());

        UUID adoptionId = UUID.randomUUID();
        ResponseEntity<Void> deleted = controller.excluir(shelter, adoptionId);
        assertEquals(HttpStatus.NO_CONTENT, deleted.getStatusCode());
        verify(service).excluir(shelter, adoptionId);

        assertEquals("/adocao", AdocaoController.class.getAnnotation(RequestMapping.class).value()[0]);
        assertNotNull(AdocaoController.class.getMethod("encerrar", Abrigo.class, UUID.class,
                com.devsouzx.adotapet.dto.request.EncerramentoAdocaoRequest.class).getAnnotation(PatchMapping.class));
        assertNotNull(AdocaoController.class.getMethod("registrar", Abrigo.class, AdocaoRequest.class)
                .getParameterAnnotations()[0][0]);
    }

    @Test
    public void controllerDePetExponeRotasDeLeituraEEscritaEStatusDeExclusao() throws Exception {
        IPetService service = mock(IPetService.class);
        PetController controller = new PetController(service);
        Abrigo shelter = Abrigo.builder().id(UUID.randomUUID()).build();
        UUID petId = UUID.randomUUID();

        ResponseEntity<Void> deleted = controller.deletePet(shelter, petId);
        assertEquals(HttpStatus.NO_CONTENT, deleted.getStatusCode());
        verify(service).removePet(petId, shelter);
        assertEquals("/pet", PetController.class.getAnnotation(RequestMapping.class).value()[0]);
        assertEquals("/novo", PetController.class.getMethod("createPet", Abrigo.class,
                com.devsouzx.adotapet.dto.request.PetRequest.class).getAnnotation(PostMapping.class).value()[0]);
        assertEquals("/{identifier}/remover", PetController.class.getMethod("deletePet", Abrigo.class, UUID.class)
                .getAnnotation(DeleteMapping.class).value()[0]);
        assertTrue(java.util.Arrays.stream(PetController.class.getMethod("createPet", Abrigo.class,
                        com.devsouzx.adotapet.dto.request.PetRequest.class).getParameterAnnotations()[0])
                .anyMatch(annotation -> annotation.annotationType() == AuthenticationPrincipal.class));
    }

    @Test
    public void endpointsDeAutenticacaoEAdotanteRetornamOsStatusDocumentados() {
        IAuthenticationService authenticationService = mock(IAuthenticationService.class);
        AuthenticationController auth = new AuthenticationController(authenticationService,
                mock(IAbrigoService.class), mock(PasswordEncoder.class), mock(TokenService.class));
        assertEquals(HttpStatus.CREATED, auth.sendRequestPasswordResetEmail(
                UserRequestResetPasswordRequest.builder().email("shelter@example.com").build()).getStatusCode());
        UUID shelterId = UUID.randomUUID();
        assertEquals(HttpStatus.CREATED, auth.resetPassword(shelterId, "code",
                UserResetPasswordRequest.builder().newPassword("new").confirmPassword("new").build()).getStatusCode());
        verify(authenticationService).sendPassswordResetEmail("shelter@example.com");

        IAdotanteService adopterService = mock(IAdotanteService.class);
        AdotanteController adopterController = new AdotanteController(adopterService);
        UUID adopterId = UUID.randomUUID();
        assertEquals(HttpStatus.NO_CONTENT, adopterController.deleteAdotante(adopterId).getStatusCode());
        verify(adopterService).deleteAdotante(adopterId);
        assertEquals("/adotante", AdotanteController.class.getAnnotation(RequestMapping.class).value()[0]);
    }

    @Test
    public void controllerDeAbrigoUsaAbrigoAutenticadoNasRotasDePerfil() throws Exception {
        IAbrigoService service = mock(IAbrigoService.class);
        AbrigoController controller = new AbrigoController(service);
        Abrigo shelter = Abrigo.builder().id(UUID.randomUUID()).build();

        assertEquals(HttpStatus.OK, controller.getAbrigoLoggedInfo(shelter).getStatusCode());
        verify(service).getAbrigoInfoById(shelter.getId());
        assertTrue(java.util.Arrays.stream(AbrigoController.class
                        .getMethod("getAbrigoLoggedInfo", Abrigo.class).getParameterAnnotations()[0])
                .anyMatch(annotation -> annotation.annotationType() == AuthenticationPrincipal.class));
        assertEquals("/proximos", AbrigoController.class
                .getMethod("getAbrigosProximos", double.class, double.class, double.class, Integer.class, Integer.class)
                .getAnnotation(GetMapping.class).value()[0]);
    }
}
