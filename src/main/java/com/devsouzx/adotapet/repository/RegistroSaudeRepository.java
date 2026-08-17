package com.devsouzx.adotapet.repository;

import com.devsouzx.adotapet.domain.registro_saude.RegistroSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegistroSaudeRepository extends JpaRepository<RegistroSaude, UUID> {
}
