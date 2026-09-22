CREATE TABLE registro_saude (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    tipo VARCHAR(50) NOT NULL,
    descricao TEXT,
    data_registro DATE NOT NULL,
    data_proximo_cuidado DATE,

    pet_id UUID NOT NULL,

    CONSTRAINT fk_registro_saude_pet
        FOREIGN KEY (pet_id)
        REFERENCES pet(id),

    CONSTRAINT chk_registro_saude_datas
        CHECK (
            data_proximo_cuidado IS NULL
            OR data_proximo_cuidado >= data_registro
        )
);