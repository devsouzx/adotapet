CREATE TABLE pet (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100),
    especie VARCHAR(50) NOT NULL,
    raca VARCHAR(100),
    sexo VARCHAR(20),
    porte VARCHAR(20),
    descricao TEXT,
    status VARCHAR(30) NOT NULL,
    idade_estimada_meses INTEGER,
    peso NUMERIC(6,2),
    foto_url TEXT,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    abrigo_id UUID NOT NULL,

    CONSTRAINT fk_pet_abrigo
        FOREIGN KEY (abrigo_id)
        REFERENCES abrigo(id),

    CONSTRAINT chk_pet_idade
        CHECK (idade_estimada_meses IS NULL OR idade_estimada_meses >= 0),

    CONSTRAINT chk_pet_peso
        CHECK (peso IS NULL OR peso > 0)
);