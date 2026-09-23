package com.devsouzx.adotapet.service.abrigo.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.endereco.Endereco;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;
import com.devsouzx.adotapet.dto.request.RegisterRequest;
import com.devsouzx.adotapet.dto.request.UserPasswordUpdateRequest;
import com.devsouzx.adotapet.dto.response.AbrigoInfoResponse;
import com.devsouzx.adotapet.dto.response.EnderecoResponse;
import com.devsouzx.adotapet.repository.AbrigoRepository;
import com.devsouzx.adotapet.repository.EnderecoRepository;
import com.devsouzx.adotapet.service.abrigo.IAbrigoService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbrigoService implements IAbrigoService {
    private final AbrigoRepository abrigoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoRepository enderecoRepository;

    public Abrigo salvarAbrigo(RegisterRequest request) {
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

    @Override
    public AbrigoInfoResponse toResponse(Abrigo abrigo) {
        return AbrigoInfoResponse.builder()
                .nome(abrigo.getNome())
                .email(abrigo.getEmail())
                .cnpj(abrigo.getCnpj())
                .telefone(abrigo.getTelefone())
                .horarioFuncionamento(abrigo.getHorarioFuncionamento())
                .descricao(abrigo.getDescricao())
                .fotoUrl(abrigo.getFotoUrl())
                .ativo(abrigo.isAtivo())
                .dataCadastro(abrigo.getDataCadastro())
                .endereco(
                        EnderecoResponse.builder()
                                .logradouro(abrigo.getEndereco().getLogradouro())
                                .cep(abrigo.getEndereco().getCep())
                                .cep(abrigo.getEndereco().getCep())
                                .numero(abrigo.getEndereco().getNumero())
                                .bairro(abrigo.getEndereco().getBairro())
                                .cidade(abrigo.getEndereco().getCidade())
                                .estado(abrigo.getEndereco().getEstado())
                                .build()
                )
                .build();
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

    public Abrigo getAbrigoByEmail(String email) throws Exception {
        return abrigoRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Abrigo não encontrado"));
    }

    public Abrigo getAbrigoById(UUID identifier) throws Exception {
        return abrigoRepository.findById(identifier).orElseThrow(() -> new RuntimeException("Abrigo não encontrado"));
    }

    public AbrigoInfoResponse getAbrigoInfoById(UUID id) throws Exception {
        Abrigo abrigo = getAbrigoById(id);

        return toResponse(abrigo);
    }

    public AbrigoInfoResponse updateAbrigo(UUID id, AbrigoUpdateRequest abrigoUpdateRequest) throws Exception {
        Abrigo abrigo =  getAbrigoById(id);

        abrigo.setNome(abrigoUpdateRequest.nome());
        abrigo.setCnpj(abrigoUpdateRequest.cnpj());
        abrigo.setHorarioFuncionamento(abrigoUpdateRequest.horarioFuncionamento());
        abrigo.setDescricao(abrigoUpdateRequest.descricao());
        abrigo.setFotoUrl(abrigoUpdateRequest.fotoUrl());
        abrigo.setTelefone(abrigoUpdateRequest.telefone());
        return getAbrigoInfoById(id);
    }

    public List<AbrigoInfoResponse> getAbrigosProximos(double latitude, double longitude, double raio) {
        raio = raio * 1000;
        List<Abrigo> abrigos = abrigoRepository.findAbrigosProximos(latitude, longitude, raio);

        return abrigos.stream().map(this::toResponse).toList();
    }
}
