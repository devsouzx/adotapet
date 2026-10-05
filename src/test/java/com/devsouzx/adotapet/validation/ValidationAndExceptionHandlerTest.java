package com.devsouzx.adotapet.validation;

import com.devsouzx.adotapet.dto.request.AdocaoRequest;
import com.devsouzx.adotapet.dto.request.AdotanteRequest;
import com.devsouzx.adotapet.dto.request.PetRequest;
import com.devsouzx.adotapet.exception.ApiErrorResponse;
import com.devsouzx.adotapet.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValidationAndExceptionHandlerTest {
    private Validator validator;
    private ValidatorFactory validatorFactory;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }

    @Test
    public void validaCamposObrigatoriosEFormatadosDoAdotante() {
        AdotanteRequest invalid = new AdotanteRequest(" ", "not-an-email", null, null);
        var violations = validator.validate(invalid);
        assertEquals(2, violations.size());

        assertTrue(validator.validate(new AdotanteRequest("Ana", "ana@example.com", null, null)).isEmpty());
    }

    @Test
    public void validaCamposObrigatoriosENumericosDoPet() {
        PetRequest invalid = new PetRequest("", " ", null, null, -1, BigDecimal.ZERO,
                null, "", "", "");
        var violations = validator.validate(invalid);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idadeEstimadaMeses")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("peso")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
        assertTrue(validator.validate(new PetRequest("Mia", "Gato", "SRD", null, 0,
                BigDecimal.ONE, null, "DISPONIVEL", "FEMEA", "PEQUENO")).isEmpty());
    }

    @Test
    public void validaIdentificadoresObrigatoriosDaAdocao() {
        assertEquals(2, validator.validate(new AdocaoRequest(null, null, null, null)).size());
        assertTrue(validator.validate(new AdocaoRequest(java.util.UUID.randomUUID(),
                java.util.UUID.randomUUID(), null, null)).isEmpty());
    }

    @Test
    public void tratadorGlobalRetornaFormatoPadraoDeErroDeRequisicaoInvalida() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/pet/abc");

        ResponseEntity<ApiErrorResponse> result = handler.handleIllegalArgument(
                new IllegalArgumentException("internal detail"), request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("INVALID_ARGUMENT", result.getBody().code());
        assertEquals("/pet/abc", result.getBody().path());
        assertTrue(result.getBody().details().isEmpty());
    }
}
