CREATE TABLE adocao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    data_adocao DATE NOT NULL,
    status VARCHAR(30) NOT NULL,
    observacoes TEXT,
    data_encerramento DATE,
    motivo_encerramento TEXT,

    abrigo_id UUID NOT NULL,
    pet_id UUID NOT NULL,
    adotante_id UUID NOT NULL,

    CONSTRAINT fk_adocao_abrigo
        FOREIGN KEY (abrigo_id)
        REFERENCES abrigo(id),

    CONSTRAINT fk_adocao_pet
        FOREIGN KEY (pet_id)
        REFERENCES pet(id),

    CONSTRAINT fk_adocao_adotante
        FOREIGN KEY (adotante_id)
        REFERENCES adotante(id),

    CONSTRAINT chk_adocao_datas
        CHECK (
            data_encerramento IS NULL
            OR data_encerramento >= data_adocao
        )
);