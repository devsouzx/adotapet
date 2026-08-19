package com.devsouzx.adotapet.service;

import com.devsouzx.adotapet.controller.AuthenticationController;
import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.endereco.Endereco;
import com.devsouzx.adotapet.dto.AuthenticationResponseDTO;
import com.devsouzx.adotapet.dto.RegisterRequestDTO;
import com.devsouzx.adotapet.dto.UserPasswordUpdateRequest;
import com.devsouzx.adotapet.repository.AbrigoRepository;
import com.devsouzx.adotapet.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbrigoService {
    private final AbrigoRepository abrigoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoRepository enderecoRepository;

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
}
