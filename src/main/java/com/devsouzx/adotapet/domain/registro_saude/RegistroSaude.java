package com.devsouzx.adotapet.domain.registro_saude;

import com.devsouzx.adotapet.domain.pet.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "registro_saude")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroSaude {
    @Id
    @GeneratedValue
    private UUID id;
    private String descricao;
    private LocalDate dataRegistro;
    private LocalDate dataProximoCuidado;

    @Enumerated(EnumType.STRING)
    private TipoRegistroSaude tipo;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
