CREATE TABLE abrigo (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    horario_funcionamento VARCHAR(255),
    descricao TEXT,
    foto_url TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endereco_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_abrigo_endereco
        FOREIGN KEY (endereco_id)
        REFERENCES endereco(id)
);