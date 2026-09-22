package com.devsouzx.adotapet.domain.adocao;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import com.devsouzx.adotapet.domain.adotante.Adotante;
import com.devsouzx.adotapet.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "adocao")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Adocao {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDate dataAdocao;
    private String observacoes;
    private LocalDate dataEncerramento;
    private String motivoEncerramento;

    @Enumerated(EnumType.STRING)
    private StatusAdocao status;

    @ManyToOne
    @JoinColumn(name = "abrigo_id")
    private Abrigo abrigo;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "adotante_id")
    private Adotante adotante;
}
