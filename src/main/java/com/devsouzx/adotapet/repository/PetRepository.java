package com.devsouzx.adotapet.repository;

import com.devsouzx.adotapet.domain.pet.Pet;
import com.devsouzx.adotapet.domain.pet.PortePet;
import com.devsouzx.adotapet.domain.pet.SexoPet;
import com.devsouzx.adotapet.domain.pet.StatusPet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
    @Query("SELECT p FROM Pet p " +
            "WHERE (:nome IS NULL OR p.nome LIKE :nome) " +
            "AND (:especie IS NULL OR p.especie LIKE :especie) " +
            "AND (:raca IS NULL OR p.raca LIKE :raca) " +
            "AND (:idadeEstimadaMeses IS NULL OR p.idadeEstimadaMeses = :idadeEstimadaMeses) " +
            "AND (:peso IS NULL OR p.peso = :peso) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:sexo IS NULL OR p.sexo = :sexo) " +
            "AND (:porte IS NULL OR p.porte = :porte)"
    )
    Page<Pet> findByFiltros(
            @Param("nome") String nome,
            @Param("especie") String especie,
            @Param("raca") String raca,
            @Param("idadeEstimadaMeses") Integer idadeEstimadaMeses,
            @Param("peso") BigDecimal peso,
            @Param("status") String status,
            @Param("sexo") String sexo,
            @Param("porte") String porte,
            Pageable pageable
    );
}
