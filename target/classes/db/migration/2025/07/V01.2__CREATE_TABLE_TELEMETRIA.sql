CREATE TABLE IF NOT EXISTS telemetria (
    id SERIAL PRIMARY KEY,
    sensor_nivel BOOLEAN NOT NULL,
    pressao_hidraulica FLOAT,
    temperatura_oleo FLOAT,
    ciclos_operacao INT,
    vibracao FLOAT,
    data_hora_coleta TIMESTAMP NOT NULL,
    nome_sensor VARCHAR(100),
    maquina_ligada BOOLEAN,
    id_maquina INT,
    CONSTRAINT fk_maquina
        FOREIGN KEY (id_maquina)
        REFERENCES maquinas(id)
        ON DELETE SET NULL
);