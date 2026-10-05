package com.devsouzx.adotapet.service.authentication.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.UserResetPasswordRequest;
import com.devsouzx.adotapet.dto.response.UserResetPasswordResponse;
import com.devsouzx.adotapet.exception.InvalidResetCodeException;
import com.devsouzx.adotapet.exception.PasswordMismatchException;
import com.devsouzx.adotapet.exception.ResetEmailUnavailableException;
import com.devsouzx.adotapet.repository.AbrigoRepository;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
import com.devsouzx.adotapet.service.redis.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.kafka.support.SendResult;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
    @Mock private RedisService redisService;
    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private IAbrigoService abrigoService;
    @Mock private AbrigoRepository abrigoRepository;
    @Mock private ObjectMapper objectMapper;
    @InjectMocks private AuthenticationService service;

    private Abrigo shelter;

    @Before
    public void setUp() {
        shelter = Abrigo.builder().id(UUID.randomUUID()).email("shelter@example.com").senha("old").build();
    }

    @Test
    public void emailDesconhecidoNaoCriaSolicitacaoNemEnviaMensagem() {
        when(abrigoService.findAbrigoByEmail("unknown@example.com")).thenReturn(Optional.empty());
        service.sendPassswordResetEmail("unknown@example.com");
        verifyNoInteractions(redisService, kafkaTemplate, objectMapper);
    }

    @Test
    public void reutilizaCodigoValidoEEnviaSolicitacaoSerializada() throws Exception {
        UserResetPasswordResponse reset = UserResetPasswordResponse.builder().abrigoId(shelter.getId())
                .email(shelter.getEmail()).resetPasswordCode("482901").build();
        when(abrigoService.findAbrigoByEmail(shelter.getEmail())).thenReturn(Optional.of(shelter));
        when(redisService.getValue("PASSWORDREQUEST_" + shelter.getEmail(), UserResetPasswordResponse.class))
                .thenReturn(reset);
        when(objectMapper.writeValueAsString(reset)).thenReturn("{\"email\":\"shelter@example.com\"}");
        when(kafkaTemplate.send(eq("abrigo-reset-password"), anyString()))
                .thenReturn(CompletableFuture.completedFuture((SendResult<String, String>) null));

        service.sendPassswordResetEmail(shelter.getEmail());

        verify(redisService, never()).setValue(anyString(), any(), any());
        verify(kafkaTemplate).send("abrigo-reset-password", "{\"email\":\"shelter@example.com\"}");
    }

    @Test
    public void armazenaCodigoDeRecuperacaoComExpiracaoQuandoAindaNaoExiste() throws Exception {
        when(abrigoService.findAbrigoByEmail(shelter.getEmail())).thenReturn(Optional.of(shelter));
        when(redisService.getValue("PASSWORDREQUEST_" + shelter.getEmail(), UserResetPasswordResponse.class)).thenReturn(null);
        when(objectMapper.writeValueAsString(any(UserResetPasswordResponse.class))).thenReturn("{}");
        when(kafkaTemplate.send(eq("abrigo-reset-password"), anyString()))
                .thenReturn(CompletableFuture.completedFuture((SendResult<String, String>) null));

        service.sendPassswordResetEmail(shelter.getEmail());

        verify(redisService).setValue(eq("PASSWORDREQUEST_" + shelter.getEmail()),
                argThat(value -> value instanceof UserResetPasswordResponse
                        && shelter.getId().equals(((UserResetPasswordResponse) value).abrigoId())),
                eq(Duration.ofMinutes(30)));
        verify(kafkaTemplate).send(eq("abrigo-reset-password"), eq("{}"));
    }

    @Test
    public void redefineSenhaValidandoCodigoEConfirmacaoEApagandoCodigo() {
        UserResetPasswordResponse reset = UserResetPasswordResponse.builder().abrigoId(shelter.getId())
                .email(shelter.getEmail()).resetPasswordCode("secret-code").build();
        when(abrigoService.getAbrigoById(shelter.getId())).thenReturn(shelter);
        when(redisService.getValue("PASSWORDREQUEST_" + shelter.getEmail(), UserResetPasswordResponse.class)).thenReturn(reset);
        when(passwordEncoder.encode("new-password")).thenReturn("encoded");

        service.resetPassword(UserResetPasswordRequest.builder().newPassword("new-password")
                .confirmPassword("new-password").build(), shelter.getId(), "secret-code");
        assertEquals("encoded", shelter.getSenha());
        verify(abrigoRepository).save(shelter);
        verify(redisService).removeKey("PASSWORDREQUEST_" + shelter.getEmail());
    }

    @Test
    public void rejeitaCodigoInvalidoESenhasDiferentesSemPersistirAlteracoes() {
        when(abrigoService.getAbrigoById(shelter.getId())).thenReturn(shelter);
        when(redisService.getValue(anyString(), eq(UserResetPasswordResponse.class))).thenReturn(null);
        UserResetPasswordRequest mismatch = UserResetPasswordRequest.builder()
                .newPassword("one").confirmPassword("two").build();
        assertThrows(InvalidResetCodeException.class,
                () -> service.resetPassword(mismatch, shelter.getId(), "bad"));

        UserResetPasswordResponse reset = UserResetPasswordResponse.builder().abrigoId(shelter.getId())
                .email(shelter.getEmail()).resetPasswordCode("valid").build();
        when(redisService.getValue("PASSWORDREQUEST_" + shelter.getEmail(), UserResetPasswordResponse.class)).thenReturn(reset);
        assertThrows(PasswordMismatchException.class,
                () -> service.resetPassword(mismatch, shelter.getId(), "valid"));
        verifyNoInteractions(passwordEncoder, abrigoRepository);
    }

    @Test
    public void informaFalhaAoEnviarMensagemPeloKafka() throws Exception {
        when(abrigoService.findAbrigoByEmail(shelter.getEmail())).thenReturn(Optional.of(shelter));
        when(redisService.getValue(anyString(), eq(UserResetPasswordResponse.class))).thenReturn(null);
        when(objectMapper.writeValueAsString(any(UserResetPasswordResponse.class))).thenReturn("{}");
        CompletableFuture<SendResult<String, String>> failed = new CompletableFuture<>();
        failed.completeExceptionally(new IllegalStateException("broker down"));
        when(kafkaTemplate.send(eq("abrigo-reset-password"), anyString())).thenReturn(failed);

        assertThrows(ResetEmailUnavailableException.class, () -> service.sendPassswordResetEmail(shelter.getEmail()));
    }
}
