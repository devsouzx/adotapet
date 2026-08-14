package com.devsouzx.adotapet.domain.pet;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "pet")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String especie;
    private String raca;
    private String descricao;
    private Integer idadeEstimadaMeses;
    private BigDecimal peso;
    private String fotoUrl;
    private LocalDateTime dataCadastro;

    @Enumerated(EnumType.STRING)
    private StatusPet status;

    @Enumerated(EnumType.STRING)
    private SexoPet sexo;

    @Enumerated(EnumType.STRING)
    private PortePet porte;

    @ManyToOne
    @JoinColumn(name = "abrigo_id")
    private Abrigo abrigo;
}
