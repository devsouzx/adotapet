package com.devsouzx.adotapet.service;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.endereco.Endereco;
import com.devsouzx.adotapet.dto.*;
import com.devsouzx.adotapet.repository.AbrigoRepository;
import com.devsouzx.adotapet.repository.EnderecoRepository;
import com.devsouzx.adotapet.service.redis.RedisService;
import com.devsouzx.adotapet.util.RandomNumberUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbrigoService {
    private final AbrigoRepository abrigoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoRepository enderecoRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisService redisService;

    public Abrigo findByEmail(String email) {
        return abrigoRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Abrigo não encontrado!"));
    }

    public Abrigo salvarAbrigoDTO(RegisterRequestDTO request) {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome(request.nome());
        abrigo.setEmail(request.email());
        if (!Objects.equals(request.senha(), request.repetirSenha())) {
            throw new RuntimeException("Senhas não coincidem");
        }
        abrigo.setSenha(passwordEncoder.encode(request.senha()));
        abrigo.setTelefone(request.telefone());
        if (request.cnpj() != null) {
            abrigo.setCnpj(request.cnpj());
        }
        abrigo.setHorarioFuncionamento(request.horarioFuncionamento());
        if (request.fotoUrl() != null) {
            abrigo.setFotoUrl(request.fotoUrl());
        }
        abrigo.setDataCadastro(LocalDateTime.now());

        Endereco endereco = new Endereco();
        endereco.setBairro(request.endereco().bairro());
        endereco.setLogradouro(request.endereco().logradouro());
        endereco.setCep(request.endereco().cep());
        endereco.setEstado(request.endereco().estado());
        endereco.setNumero(request.endereco().numero());
        endereco.setCidade(request.endereco().cidade());
        endereco.setLongitude(new BigDecimal("-16.6869"));
        endereco.setLatitude(new BigDecimal("-16.6869"));

        endereco = enderecoRepository.save(endereco);
        abrigo.setEndereco(endereco);

        return abrigoRepository.save(abrigo);
    }

    public void atualizarSenha(UserPasswordUpdateRequest request, UUID userIdentifier) {
        Abrigo abrigo = abrigoRepository.findById(userIdentifier).orElseThrow(() -> new RuntimeException("Abrigo não encontrado"));

        if (!passwordEncoder.matches(request.senhaAtual(), abrigo.getSenha())) {
            throw new RuntimeException("Senha atual incorreta!");
        }

        if (!Objects.equals(request.novaSenha(), request.confirmarNovaSenha())) {
            throw new RuntimeException("Senhas não coincidem!");
        }

        String novaSenha = passwordEncoder.encode(request.novaSenha());
        abrigo.setSenha(novaSenha);

        abrigoRepository.save(abrigo);
    }

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
        Abrigo abrigo = getAbrigoByEmail(email);
        String resetPasswordUrl = "http://localhost:8080/auth/resetpassword/?id=" + abrigo.getId() + "&hash=" + userResetPasswordResponse.getResetPasswordCode();
        log.error(resetPasswordUrl);
    }

    @Transactional
    public void resetPassword(UserResetPasswordRequest request, UUID id, String code) throws Exception {
        Abrigo abrigo = abrigoRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

        UserResetPasswordResponse userResetPasswordResponse = (UserResetPasswordResponse) redisService.getValue("PASSWORDREQUEST_" + abrigo.getEmail(), UserResetPasswordResponse.class);
        if (userResetPasswordResponse == null) throw new Exception("UserResetPasswordResponse does not exists");

        if (!request.getNewPassword().equals(request.getConfirmPassword())) throw new IllegalArgumentException("The passwords you entered were not identical. Please try again.");

        abrigo.setSenha(passwordEncoder.encode(request.getConfirmPassword()));
        abrigoRepository.save(abrigo);

        redisService.removeKey("PASSWORDREQUEST_" + userResetPasswordResponse.getEmail());
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

    public Abrigo getAbrigoByEmail(String email) throws Exception {
        return abrigoRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Abrigo não encontrado"));
    }
}
