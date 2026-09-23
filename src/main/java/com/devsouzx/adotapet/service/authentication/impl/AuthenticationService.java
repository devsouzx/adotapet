package com.devsouzx.adotapet.service.authentication.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.dto.request.UserResetPasswordRequest;
import com.devsouzx.adotapet.dto.response.UserResetPasswordResponse;
import com.devsouzx.adotapet.repository.AbrigoRepository;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
import com.devsouzx.adotapet.service.abrigo.impl.AbrigoService;
import com.devsouzx.adotapet.service.authentication.IAuthenticationService;
import com.devsouzx.adotapet.service.redis.RedisService;
import com.devsouzx.adotapet.util.RandomNumberUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void sendPassswordResetEmail(String email) throws Exception {
        UserResetPasswordResponse userResetPasswordResponse =
                (UserResetPasswordResponse) redisService.getValue("PASSWORDREQUEST_" + email, UserResetPasswordResponse.class);
        if (userResetPasswordResponse == null) {
            userResetPasswordResponse = UserResetPasswordResponse.builder()
                    .email(email)
                    .resetPasswordCode(RandomNumberUtil.generateRandomCode())
                    .build();

            redisService.setValue("PASSWORDREQUEST_" + email, userResetPasswordResponse, TimeUnit.MILLISECONDS, 1800000L);
        }

        trySendKafkaMessage(userResetPasswordResponse.toString(), "abrigo-reset-password");
        Abrigo abrigo = iAbrigoService.getAbrigoByEmail(email);
        String resetPasswordUrl = "http://localhost:8080/auth/resetpassword/?id=" + abrigo.getId() + "&hash=" + userResetPasswordResponse.resetPasswordCode();
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
    }

    @Transactional
    private void trySendKafkaMessage(String email, String TOPIC) throws Exception {
        try {
            kafkaTemplate.send(TOPIC, email);
            log.error("Mensagem enviada com SUCESSO para o tópico: {}", TOPIC);
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para o topico {}", TOPIC);
        }
    }
}
