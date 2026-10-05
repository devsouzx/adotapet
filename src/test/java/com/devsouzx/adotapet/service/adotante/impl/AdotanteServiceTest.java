package com.devsouzx.adotapet.service.adotante.impl;

import com.devsouzx.adotapet.domain.adotante.Adotante;
import com.devsouzx.adotapet.dto.request.AdotanteRequest;
import com.devsouzx.adotapet.dto.response.AdotanteResponse;
import com.devsouzx.adotapet.exception.AdopterNotFoundException;
import com.devsouzx.adotapet.exception.InvalidPaginationException;
import com.devsouzx.adotapet.repository.AdotanteRepository;
import com.devsouzx.adotapet.service.redis.RedisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdotanteServiceTest {
    @Mock private AdotanteRepository repository;
    @Mock private RedisService redisService;
    @InjectMocks private AdotanteService service;

    private UUID id;
    private Adotante adopter;

    @Before
    public void setUp() {
        id = UUID.randomUUID();
        adopter = Adotante.builder().id(id).nome("Ana").email("ana@example.com")
                .telefone("11912345678").dataNascimento(LocalDate.of(1990, 5, 20)).build();
    }

    @Test
    public void listaAdotantesComPaginacaoValidadaEConverteResposta() {
        when(repository.findAll(PageRequest.of(1, 10))).thenReturn(new PageImpl<>(List.of(adopter)));
        assertEquals("Ana", service.getAdotantes(1, 10).getContent().get(0).nome());
        assertEquals(id, service.getAdotantes(1, 10).getContent().get(0).id());

        assertThrows(InvalidPaginationException.class, () -> service.getAdotantes(null, 10));
        assertThrows(InvalidPaginationException.class, () -> service.getAdotantes(0, 101));
        verify(repository, times(2)).findAll(PageRequest.of(1, 10));
    }

    @Test
    public void cadastraAdotanteEArmazenaRespostaNoCachePorDezMinutos() {
        when(repository.save(any(Adotante.class))).thenAnswer(invocation -> {
            Adotante saved = invocation.getArgument(0);
            saved.setId(id);
            return saved;
        });
        AdotanteResponse response = service.createAdotante(request("Ana"));
        assertEquals(id, response.id());
        assertEquals("ana@example.com", response.email());
        verify(redisService).setValue("ADOTANTE_" + id, response, Duration.ofMinutes(10));
    }

    @Test
    public void consultaCacheSemAcessarRepositorioEPreencheQuandoVazio() {
        AdotanteResponse cached = service.toResponse(adopter);
        when(redisService.getValue("ADOTANTE_" + id, AdotanteResponse.class)).thenReturn(cached);
        assertSame(cached, service.getAdotanteById(id));
        verifyNoInteractions(repository);

        when(redisService.getValue("ADOTANTE_" + id, AdotanteResponse.class)).thenReturn(null);
        when(repository.findById(id)).thenReturn(Optional.of(adopter));
        assertEquals("Ana", service.getAdotanteById(id).nome());
        verify(redisService).setValue("ADOTANTE_" + id, cached, Duration.ofMinutes(10));
    }

    @Test
    public void atualizaEExcluiAdotanteInvalidandoValorEmCache() {
        when(repository.findById(id)).thenReturn(Optional.of(adopter));
        when(repository.save(adopter)).thenReturn(adopter);
        AdotanteResponse response = service.updateAdotante(id, request("Ana Nova"));
        assertEquals("Ana Nova", response.nome());
        verify(redisService).removeKey("ADOTANTE_" + id);

        service.deleteAdotante(id);
        verify(repository).delete(adopter);
        verify(redisService, times(2)).removeKey("ADOTANTE_" + id);
    }

    @Test
    public void informaQuandoAdotanteNaoExisteAoConsultarAtualizarOuExcluir() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        when(redisService.getValue("ADOTANTE_" + id, AdotanteResponse.class)).thenReturn(null);
        assertThrows(AdopterNotFoundException.class, () -> service.getAdotanteById(id));
        assertThrows(AdopterNotFoundException.class, () -> service.updateAdotante(id, request("Ana")));
        assertThrows(AdopterNotFoundException.class, () -> service.deleteAdotante(id));
    }

    private AdotanteRequest request(String name) {
        return new AdotanteRequest(name, "ana@example.com", "11912345678", LocalDate.of(1990, 5, 20));
    }
}
