package com.devsouzx.adotapet.repository;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbrigoRepository extends JpaRepository<Abrigo, UUID> {
}
