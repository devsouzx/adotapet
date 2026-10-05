package com.devsouzx.adotapet.infra.config;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.Assert.*;

public class TokenServiceTest {
    private TokenService service;

    @Before
    public void setUp() throws Exception {
        service = new TokenService();
        Field secret = TokenService.class.getDeclaredField("secret");
        secret.setAccessible(true);
        secret.set(service, "test-secret-key-with-enough-entropy");
    }

    @Test
    public void tokenGeradoEValidadoRetornaEmailDoAbrigo() {
        Abrigo shelter = Abrigo.builder().id(UUID.randomUUID()).email("shelter@example.com").build();
        String token = service.generateToken(shelter);
        assertEquals("shelter@example.com", service.validateToken(token));
    }

    @Test
    public void tokensInvalidosOuDeOutroSegredoSaoRejeitados() {
        String token = service.generateToken(Abrigo.builder().email("shelter@example.com").build());
        assertNull(service.validateToken("not-a-jwt"));

        TokenService other = new TokenService();
        try {
            Field secret = TokenService.class.getDeclaredField("secret");
            secret.setAccessible(true);
            secret.set(other, "different-secret");
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
        assertNull(other.validateToken(token));
    }

    @Test
    public void expiracaoGeradaOcorreAproximadamenteDuasHorasDepois() {
        Instant before = Instant.now();
        Instant expiration = service.generateExpirationTime();
        Instant after = Instant.now();
        assertTrue(expiration.isAfter(before.plus(119, ChronoUnit.MINUTES)));
        assertTrue(expiration.isBefore(after.plus(121, ChronoUnit.MINUTES)));
    }
}
