package com.devsouzx.adotapet.domain.abrigo;

import com.devsouzx.adotapet.domain.endereco.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "abrigo")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Abrigo {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cnpj;
    private String horarioFuncionamento;
    private String descricao;
    private String fotoUrl;
    private boolean ativo;
    private LocalDateTime dataCadastro;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
