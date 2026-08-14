package com.devsouzx.adotapet.domain.endereco;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "endereco")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue
    private UUID id;
    private String logradouro;
    private String cep;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
