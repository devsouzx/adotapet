-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS adotapet;

-- Usar o banco de dados
USE adotapet;

-- REMOVER TABELAS SE EXISTIREM
DROP TABLE IF EXISTS avaliacao;
DROP TABLE IF EXISTS solicitacao_adocao;
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS abrigo;
DROP TABLE IF EXISTS adotante;
DROP TABLE IF EXISTS endereco;

-- TABELA: endereco
CREATE TABLE endereco (
    id INT PRIMARY KEY AUTO_INCREMENT,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    bairro VARCHAR(50) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(9) NOT NULL
);

-- TABELA: adotante
CREATE TABLE adotante (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    endereco_id INT NOT NULL,
    data_cadastro DATETIME NOT NULL,
    FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

-- TABELA: abrigo
CREATE TABLE abrigo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    nome_responsavel VARCHAR(100) NOT NULL,
    horario_funcionamento VARCHAR(100),
    endereco_id INT NOT NULL,
    data_cadastro DATETIME NOT NULL,
    FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

-- TABELA: pet
CREATE TABLE pet (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    especie VARCHAR(20) NOT NULL,
    raca VARCHAR(50),
    idade_meses INT NOT NULL,
    porte VARCHAR(10) NOT NULL,
    descricao TEXT,
    foto VARCHAR(255),
    status VARCHAR(15) NOT NULL DEFAULT 'DISPONIVEL',
    abrigo_id INT NOT NULL,
    FOREIGN KEY (abrigo_id) REFERENCES abrigo(id)
);

-- TABELA: solicitacao_adocao
CREATE TABLE solicitacao_adocao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_solicitacao DATETIME NOT NULL,
    data_resposta DATETIME,
    status VARCHAR(25) NOT NULL DEFAULT 'PENDENTE',
    justificativa TEXT,
    adotante_id INT NOT NULL,
    pet_id INT NOT NULL,
    FOREIGN KEY (adotante_id) REFERENCES adotante(id),
    FOREIGN KEY (pet_id) REFERENCES pet(id)
);

-- TABELA: avaliacao
CREATE TABLE avaliacao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nota INT NOT NULL,
    comentario TEXT,
    data_avaliacao DATETIME NOT NULL,
    adotante_id INT NOT NULL,
    abrigo_id INT NOT NULL,
    solicitacao_id INT NOT NULL UNIQUE,
    FOREIGN KEY (adotante_id) REFERENCES adotante(id),
    FOREIGN KEY (abrigo_id) REFERENCES abrigo(id),
    FOREIGN KEY (solicitacao_id) REFERENCES solicitacao_adocao(id),
    CHECK (nota BETWEEN 1 AND 5)
);
