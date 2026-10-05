CREATE UNIQUE INDEX uq_adocao_pet_ativa
    ON adocao (pet_id)
    WHERE status = 'ATIVA';
