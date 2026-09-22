CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE endereco (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    logradouro VARCHAR(150) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    latitude NUMERIC(9,6),
    longitude NUMERIC(9,6)
);