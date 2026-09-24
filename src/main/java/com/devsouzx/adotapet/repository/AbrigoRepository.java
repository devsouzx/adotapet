package com.devsouzx.adotapet.repository;

import com.devsouzx.adotapet.domain.abrigo.Abrigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbrigoRepository extends JpaRepository<Abrigo, UUID> {
    Optional<Abrigo> findByEmail(String email);

    @Query(value = """
            SELECT a.*
            FROM abrigo a
            JOIN endereco e ON e.id = a.endereco_id
            WHERE e.latitude IS NOT NULL
              AND e.longitude IS NOT NULL
              AND ST_DWithin(
                  ST_SetSRID(ST_MakePoint(e.longitude, e.latitude), 4326)::geography,
                  ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                  :raio
              )
            """, nativeQuery = true)
    Page<Abrigo> findAbrigosProximos(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("raio") double raio,
            Pageable pageable
    );
}
