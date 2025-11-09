-- V003__create_audit_table.sql
-- Configurações e views auxiliares
-- Obs: auditoria de motos já existe no SQL original (Auditoria_Moto + trigger)

-- Sequência de versão
CREATE SEQUENCE seq_versao_sistema START WITH 1 INCREMENT BY 1;

-- Configurações do sistema
CREATE TABLE Sistema_config (
chave VARCHAR2(100) PRIMARY KEY,
valor VARCHAR2(500) NOT NULL,
descricao VARCHAR2(255),
data_criacao TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
data_atualizacao TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

INSERT INTO Sistema_config (chave, valor, descricao) VALUES
('VERSAO_SISTEMA', '1.0.0', 'Versão atual do sistema');

INSERT INTO Sistema_config (chave, valor, descricao) VALUES
('DIAS_MANTER_HISTORICO', '90', 'Dias para manter histórico de posições');

INSERT INTO Sistema_config (chave, valor, descricao) VALUES
('DIAS_MANTER_ALERTAS', '30', 'Dias para manter alertas');

-- Trigger de atualização
CREATE OR REPLACE TRIGGER trg_sistema_config_update
BEFORE UPDATE ON Sistema_config
FOR EACH ROW
BEGIN
:NEW.data_atualizacao := SYSTIMESTAMP;
END;
/

-- Views auxiliares
CREATE OR REPLACE VIEW vw_motos_resumo AS
SELECT m.id_moto, m.placa, m.status,
md.fabricante, md.nome_modelo,
p.nome_patio, s.tipo_sensor, s.bateria_percentual
FROM Moto m
LEFT JOIN Modelo md ON m.Modelo_id_modelo = md.id_modelo
LEFT JOIN Patio p ON m.id_patio = p.id_patio
LEFT JOIN Sensor_iot s ON m.id_sensor_iot = s.id_sensor_iot;

CREATE OR REPLACE VIEW vw_alertas_recentes AS
SELECT a.id_alerta, a.tipo_alerta, a.data_geracao, m.placa
FROM Alerta_evento a
JOIN Moto m ON a.id_moto = m.id_moto
WHERE a.data_geracao >= SYSTIMESTAMP - INTERVAL '7' DAY
ORDER BY a.data_geracao DESC;

COMMIT;
