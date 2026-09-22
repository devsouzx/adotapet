package com.devsouzx.adotapet.repository;

import com.devsouzx.adotapet.domain.adotante.Adotante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdotanteRepository extends JpaRepository<Adotante, UUID> {
}
