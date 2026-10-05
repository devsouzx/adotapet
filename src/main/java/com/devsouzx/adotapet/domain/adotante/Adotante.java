package com.devsouzx.adotapet.domain.adotante;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "adotante")
@Entity
@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Adotante {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
}
