package com.devsouzx.adotapet.repository;

import com.devsouzx.adotapet.domain.adocao.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdocaoRepository extends JpaRepository<Adocao, UUID> {
}
