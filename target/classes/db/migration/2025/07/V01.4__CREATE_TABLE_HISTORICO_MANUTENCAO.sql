CREATE TABLE historico_manutencao (
    id SERIAL PRIMARY KEY,
    id_maquina INT,
    tipo_manutencao VARCHAR(100),
    acao_realizada TEXT,
    data_hora_manutencao TIMESTAMP,
    tempo_manutencao_min INT,
    id_falha INT,
    CONSTRAINT fk_manutencao_maquina
        FOREIGN KEY (id_maquina)
        REFERENCES maquinas(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_manutencao_falha
        FOREIGN KEY (id_falha)
        REFERENCES historico_falhas(id)
        ON DELETE SET NULL
);