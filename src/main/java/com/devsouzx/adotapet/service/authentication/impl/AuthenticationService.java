package com.devsouzx.adotapet.service.authentication.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.UserResetPasswordRequest;
import com.devsouzx.adotapet.dto.response.UserResetPasswordResponse;
import com.devsouzx.adotapet.repository.AbrigoRepository;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
import com.devsouzx.adotapet.service.authentication.IAuthenticationService;
import com.devsouzx.adotapet.service.redis.RedisService;
import com.devsouzx.adotapet.util.RandomNumberUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final RedisService redisService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;
    private final IAbrigoService iAbrigoService;
    private final AbrigoRepository abrigoRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void sendPassswordResetEmail(String email) throws Exception {
        Abrigo abrigo = iAbrigoService.getAbrigoByEmail(email);
        UserResetPasswordResponse userResetPasswordResponse =
                (UserResetPasswordResponse) redisService.getValue("PASSWORDREQUEST_" + email, UserResetPasswordResponse.class);
        if (userResetPasswordResponse == null
                || !abrigo.getId().equals(userResetPasswordResponse.abrigoId())
                || !email.equals(userResetPasswordResponse.email())) {
            userResetPasswordResponse = UserResetPasswordResponse.builder()
                    .abrigoId(abrigo.getId())
                    .email(email)
                    .resetPasswordCode(RandomNumberUtil.generateRandomCode())
                    .build();

            redisService.setValue("PASSWORDREQUEST_" + email, userResetPasswordResponse, TimeUnit.MILLISECONDS, 1800000L);
        }

        trySendKafkaMessage(objectMapper.writeValueAsString(userResetPasswordResponse), "abrigo-reset-password");
    }

    @Transactional
    public void resetPassword(UserResetPasswordRequest request, UUID id, String code) throws Exception {
        Abrigo abrigo = iAbrigoService.getAbrigoById(id);

        UserResetPasswordResponse userResetPasswordResponse = (UserResetPasswordResponse) redisService.getValue("PASSWORDREQUEST_" + abrigo.getEmail(), UserResetPasswordResponse.class);
        if (userResetPasswordResponse == null) throw new Exception("UserResetPasswordResponse does not exists");

        if (!request.newPassword().equals(request.confirmPassword())) throw new IllegalArgumentException("The passwords you entered were not identical. Please try again.");

        abrigo.setSenha(passwordEncoder.encode(request.confirmPassword()));
        abrigoRepository.save(abrigo);

        redisService.removeKey("PASSWORDREQUEST_" + userResetPasswordResponse.email());
        redisService.removeKey("ABRIGO_" + abrigo.getId());
        redisService.removeKey("ABRIGO_" + abrigo.getEmail());
    }

    private void trySendKafkaMessage(String message, String topic) throws Exception {
        try {
            kafkaTemplate.send(topic, message).get();
            log.info("Mensagem enviada com sucesso para o tópico: {}", topic);
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para o tópico {}", topic, e);
            throw e;
        }
    }
}
