package com.devsouzx.adotapet.service.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisServiceTest {
    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> values;
    @Mock private ObjectMapper objectMapper;
    private RedisService service;

    @Before
    public void setUp() {
        service = new RedisService(redisTemplate, objectMapper);
        when(redisTemplate.opsForValue()).thenReturn(values);
    }

    @Test
    public void serializaEArmazenaComTempoDeExpiracaoSolicitado() throws Exception {
        Object payload = new Object();
        when(objectMapper.writeValueAsString(payload)).thenReturn("{\"value\":1}");
        Duration ttl = Duration.ofMinutes(3);
        service.setValue("cache-key", payload, ttl);
        verify(values).set("cache-key", "{\"value\":1}", ttl);
    }

    @Test
    public void retornaNuloQuandoNaoHaValorEConverteValorEncontradoNoCache() throws Exception {
        when(values.get("missing")).thenReturn(null);
        assertNull(service.getValue("missing", CacheValue.class));
        verifyNoInteractions(objectMapper);

        when(values.get("present")).thenReturn("{\"name\":\"Mia\"}");
        CacheValue result = new CacheValue("Mia");
        when(objectMapper.readValue("{\"name\":\"Mia\"}", CacheValue.class)).thenReturn(result);
        assertSame(result, service.getValue("present", CacheValue.class));
    }

    @Test
    public void converteFalhasDeSerializacaoEDesserializacaoEmErroDeEstado() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenThrow(new IllegalStateException("cannot encode"));
        IllegalStateException writeFailure = assertThrows(IllegalStateException.class,
                () -> service.setValue("key", "value", Duration.ofSeconds(2)));
        assertEquals("Erro ao serializar valor para o Redis", writeFailure.getMessage());

        when(values.get("key")).thenReturn("invalid");
        when(objectMapper.readValue("invalid", CacheValue.class)).thenThrow(new IllegalArgumentException("bad json"));
        IllegalStateException readFailure = assertThrows(IllegalStateException.class,
                () -> service.getValue("key", CacheValue.class));
        assertEquals("Erro ao desserializar valor do Redis", readFailure.getMessage());
    }

    @Test
    public void removeAChaveSolicitada() {
        service.removeKey("expired");
        verify(redisTemplate).delete("expired");
    }

    public static class CacheValue {
        public String name;
        public CacheValue() {}
        CacheValue(String name) { this.name = name; }
    }
}
