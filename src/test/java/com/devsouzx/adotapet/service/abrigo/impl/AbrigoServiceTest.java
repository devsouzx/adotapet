package com.devsouzx.adotapet.service.abrigo.impl;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.endereco.Endereco;
import com.devsouzx.adotapet.dto.request.AbrigoUpdateRequest;
import com.devsouzx.adotapet.dto.request.EnderecoRequest;
import com.devsouzx.adotapet.dto.request.RegisterRequest;
import com.devsouzx.adotapet.exception.*;
import com.devsouzx.adotapet.repository.AbrigoRepository;
import com.devsouzx.adotapet.repository.EnderecoRepository;
import com.devsouzx.adotapet.service.redis.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AbrigoServiceTest {
    @Mock private AbrigoRepository abrigoRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EnderecoRepository enderecoRepository;
    @Mock private RedisService redisService;
    @InjectMocks private AbrigoService service;

    private Abrigo abrigo;

    @Before
    public void setUp() {
        Endereco endereco = Endereco.builder().id(UUID.randomUUID()).logradouro("Rua 1")
                .cep("12345").numero("10").bairro("Centro").cidade("São Paulo").estado("SP")
                .latitude(new BigDecimal("-23.5")).longitude(new BigDecimal("-46.6")).build();
        abrigo = Abrigo.builder().id(UUID.randomUUID()).nome("Abrigo A").email("a@example.com")
                .senha("hash").telefone("11999999999").endereco(endereco).build();
    }

    @Test
    public void cadastraAbrigoEArmazenaNoCachePorIdEEmail() {
        RegisterRequest request = registerRequest("segredo", "segredo");
        when(passwordEncoder.encode("segredo")).thenReturn("hash");
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(abrigoRepository.save(any(Abrigo.class))).thenAnswer(invocation -> {
            Abrigo saved = invocation.getArgument(0);
            saved.setId(abrigo.getId());
            return saved;
        });

        Abrigo registered = service.salvarAbrigo(request);

        assertEquals("Abrigo A", registered.getNome());
        assertEquals("hash", registered.getSenha());
        assertEquals("Centro", registered.getEndereco().getBairro());
        assertNotNull(registered.getDataCadastro());
        verify(redisService).setValue(eq("ABRIGO_" + registered.getId()), eq(registered), eq(Duration.ofMinutes(10)));
        verify(redisService).setValue(eq("ABRIGO_" + registered.getEmail()), eq(registered), eq(Duration.ofMinutes(10)));
    }

    @Test
    public void rejeitaSenhasDiferentesAntesDePersistirCadastro() {
        assertThrows(PasswordMismatchException.class, () -> service.salvarAbrigo(registerRequest("a", "b")));
        verifyNoInteractions(enderecoRepository, abrigoRepository, redisService);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void buscaAbrigoNoCacheOuRepositorioEConsultaEmailOpcionalmente() {
        when(redisService.getValue("ABRIGO_a@example.com", Abrigo.class)).thenReturn(abrigo);
        assertSame(abrigo, service.getAbrigoByEmail("a@example.com"));
        verifyNoInteractions(abrigoRepository);

        when(redisService.getValue("ABRIGO_" + abrigo.getId(), Abrigo.class)).thenReturn(null);
        when(abrigoRepository.findById(abrigo.getId())).thenReturn(Optional.of(abrigo));
        assertSame(abrigo, service.getAbrigoById(abrigo.getId()));
        verify(redisService).setValue("ABRIGO_" + abrigo.getId(), abrigo, Duration.ofMinutes(10));

        when(abrigoRepository.findByEmail("a@example.com")).thenReturn(Optional.of(abrigo));
        assertEquals(abrigo, service.findAbrigoByEmail("a@example.com").get());
    }

    @Test
    public void informaQuandoAbrigoNaoExiste() {
        when(redisService.getValue("ABRIGO_missing@example.com", Abrigo.class)).thenReturn(null);
        when(abrigoRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        assertThrows(ShelterNotFoundException.class, () -> service.getAbrigoByEmail("missing@example.com"));
    }

    @Test
    public void atualizaAbrigoEInvalidaTodasAsChavesRelacionadasDoCache() {
        when(redisService.getValue("ABRIGO_" + abrigo.getId(), Abrigo.class)).thenReturn(abrigo);
        when(abrigoRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(abrigoRepository.save(any(Abrigo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AbrigoUpdateRequest update = new AbrigoUpdateRequest("Updated", "new@example.com", "123",
                "cnpj", "9-5", "description", "photo");

        assertEquals("Updated", service.updateAbrigo(abrigo.getId(), update).nome());
        assertEquals("new@example.com", abrigo.getEmail());
        verify(redisService).removeKey("ABRIGO_" + abrigo.getId());
        verify(redisService).removeKey("ABRIGO_a@example.com");
        verify(redisService).removeKey("ABRIGO_new@example.com");
    }

    @Test
    public void rejeitaEmailJaUtilizadoPorOutroAbrigo() {
        Abrigo other = Abrigo.builder().id(UUID.randomUUID()).email("used@example.com").build();
        when(redisService.getValue("ABRIGO_" + abrigo.getId(), Abrigo.class)).thenReturn(abrigo);
        when(abrigoRepository.findByEmail("used@example.com")).thenReturn(Optional.of(other));
        assertThrows(EmailAlreadyRegisteredException.class, () -> service.updateAbrigo(abrigo.getId(),
                new AbrigoUpdateRequest("Updated", "used@example.com", null, null, null, null, null)));
        verify(abrigoRepository, never()).save(any());
    }

    @Test
    public void validaCoordenadasRaioEPaginacaoNaBuscaPorProximidade() {
        assertThrows(InvalidCoordinatesException.class, () -> service.getAbrigosProximos(91, 0, 5, 0, 10));
        assertThrows(InvalidCoordinatesException.class, () -> service.getAbrigosProximos(0, Double.NaN, 5, 0, 10));
        assertThrows(InvalidSearchRadiusException.class, () -> service.getAbrigosProximos(0, 0, 0, 0, 10));
        assertThrows(InvalidPaginationException.class, () -> service.getAbrigosProximos(0, 0, 5, -1, 10));
        assertThrows(InvalidPaginationException.class, () -> service.getAbrigosProximos(0, 0, 5, 0, 101));
        verifyNoInteractions(abrigoRepository);

        when(abrigoRepository.findAbrigosProximos(eq(-23.5), eq(-46.6), eq(5000.0), eq(PageRequest.of(1, 5))))
                .thenReturn(new PageImpl<>(List.of(abrigo)));
        assertEquals(1, service.getAbrigosProximos(-23.5, -46.6, 5, 1, 5).getTotalElements());
    }

    private RegisterRequest registerRequest(String password, String confirmation) {
        return new RegisterRequest("Abrigo A", "a@example.com", password, confirmation, "11999999999",
                null, "9-5", "description", null,
                new EnderecoRequest("Rua 1", "12345", "10", "Centro", "São Paulo", "SP",
                        new BigDecimal("-23.5"), new BigDecimal("-46.6")));
    }
}
