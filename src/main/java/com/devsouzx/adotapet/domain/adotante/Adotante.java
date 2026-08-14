package com.devsouzx.adotapet.domain.adotante;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "adotante")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Adotante {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
}
