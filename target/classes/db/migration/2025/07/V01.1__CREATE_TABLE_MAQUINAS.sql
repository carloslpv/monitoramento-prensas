CREATE TABLE maquinas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    data_compra DATE not NULL
);