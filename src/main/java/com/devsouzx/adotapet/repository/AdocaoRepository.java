package com.devsouzx.adotapet.repository;

import com.devsouzx.adotapet.domain.adocao.Adocao;
import com.devsouzx.adotapet.domain.adocao.StatusAdocao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdocaoRepository extends JpaRepository<Adocao, UUID> {
    Page<Adocao> findAllByAbrigoIdOrderByDataAdocaoDesc(UUID abrigoId, Pageable pageable);
    Optional<Adocao> findByIdAndAbrigoId(UUID id, UUID abrigoId);
    boolean existsByPetIdAndStatus(UUID petId, StatusAdocao status);
}
