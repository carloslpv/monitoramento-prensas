CREATE TABLE historico_falhas (
    id SERIAL PRIMARY KEY,
    id_maquina INT NOT NULL,
    tipo_falha VARCHAR(100) NOT NULL,
    data_falha TIMESTAMP,
    CONSTRAINT fk_maquina
        FOREIGN KEY (id_maquina)
        REFERENCES maquinas(id)
        ON DELETE CASCADE
)