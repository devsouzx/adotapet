package com.devsouzx.adotapet.domain.abrigo;

import com.devsouzx.adotapet.domain.endereco.Endereco;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "abrigo")
@Entity
@Getter
@Setter
@Builder
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
