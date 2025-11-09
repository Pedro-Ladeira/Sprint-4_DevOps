-- Script convertido de Oracle PL/SQL para PostgreSQL (PL/pgSQL)
-- Observação: alguns recursos (como DBMS_SCHEDULER) não têm equivalente direto no Postgres padrão
-- e precisam ser agendados externamente (pg_cron, pgagent, etc.).

-- DROP TABLEs (ignora erro se não existir)
DROP TABLE IF EXISTS Historico_posicao CASCADE;
DROP TABLE IF EXISTS Alerta_evento CASCADE;
DROP TABLE IF EXISTS Sensor_iot CASCADE;
DROP TABLE IF EXISTS Moto CASCADE;
DROP TABLE IF EXISTS Usuario_sistema CASCADE;
DROP TABLE IF EXISTS Modelo CASCADE;
DROP TABLE IF EXISTS Patio CASCADE;
DROP TABLE IF EXISTS Auditoria_Moto CASCADE;

-- Criação das tabelas baseadas no diagrama ER (tipos ajustados para PostgreSQL)
CREATE TABLE Usuario_sistema (
    id_usuario INTEGER PRIMARY KEY,
    nome varchar(50),
    email varchar(100),
    senha varchar(20)
);

CREATE TABLE Modelo (
    id_modelo INTEGER PRIMARY KEY,
    fabricante varchar(50),
    nome_modelo varchar(50),
    cilindrada INTEGER,
    tipo varchar(30)
);

CREATE TABLE Patio (
    id_patio INTEGER PRIMARY KEY,
    nome_patio varchar(50),
    localizacao_patio varchar(50),
    area_total numeric(10,2),
    capacidade_maxima INTEGER
);

CREATE TABLE Sensor_iot (
    id_sensor_iot INTEGER PRIMARY KEY,
    tipo_sensor varchar(20),
    data_transmissao timestamp,
    bateria_percentual numeric(5,2),
    id_moto INTEGER
);

CREATE TABLE Moto (
    id_moto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    placa varchar(10),
    chassi varchar(50),
    ano_fabricacao INTEGER,
    status varchar(20),
    Modelo_id_modelo INTEGER,
    id_patio INTEGER,
    id_sensor_iot INTEGER,
    data_atualizacao timestamp,
    CONSTRAINT fk_modelo FOREIGN KEY (Modelo_id_modelo) REFERENCES Modelo(id_modelo),
    CONSTRAINT fk_patio FOREIGN KEY (id_patio) REFERENCES Patio(id_patio),
    CONSTRAINT fk_sensor_iot FOREIGN KEY (id_sensor_iot) REFERENCES Sensor_iot(id_sensor_iot)
);

-- Adiciona chave estrangeira ao Sensor_iot após criação da tabela Moto
ALTER TABLE Sensor_iot
    ADD CONSTRAINT fk_moto_sensor FOREIGN KEY (id_moto) REFERENCES Moto(id_moto);

CREATE TABLE Historico_posicao (
    data_atualizacao timestamp,
    posicao_x numeric(10,2),
    posicao_y numeric(10,2),
    acuracia_localizacao numeric(5,2),
    origem_detectada varchar(20),
    status_no_momento varchar(20),
    id_moto INTEGER,
    CONSTRAINT fk_moto_historico FOREIGN KEY (id_moto) REFERENCES Moto(id_moto),
    CONSTRAINT pk_historico PRIMARY KEY (data_atualizacao, id_moto)
);

CREATE TABLE Alerta_evento (
    id_alerta INTEGER PRIMARY KEY,
    tipo_alerta varchar(50),
    data_geracao timestamp,
    id_moto INTEGER,
    CONSTRAINT fk_moto_alerta FOREIGN KEY (id_moto) REFERENCES Moto(id_moto)
);

---------------------------------------------------------------------------------------

-- Inserção de dados em Usuario_sistema
INSERT INTO Usuario_sistema VALUES (1, 'João Silva', 'joao.silva@email.com', 'senha123');
INSERT INTO Usuario_sistema VALUES (2, 'Maria Souza', 'maria.souza@email.com', 'senha456');
INSERT INTO Usuario_sistema VALUES (3, 'Pedro Oliveira', 'pedro.oliveira@email.com', 'senha789');
INSERT INTO Usuario_sistema VALUES (4, 'Ana Santos', 'ana.santos@email.com', 'senha101');
INSERT INTO Usuario_sistema VALUES (5, 'Carlos Ferreira', 'carlos.ferreira@email.com', 'senha202');

-- Inserção de dados em Modelo
INSERT INTO Modelo VALUES (1, 'Honda', 'CG 160', 160, 'Street');
INSERT INTO Modelo VALUES (2, 'Yamaha', 'Fazer 250', 250, 'Street');
INSERT INTO Modelo VALUES (3, 'BMW', 'G 310 GS', 310, 'Adventure');
INSERT INTO Modelo VALUES (4, 'Kawasaki', 'Ninja 400', 400, 'Sport');
INSERT INTO Modelo VALUES (5, 'Harley-Davidson', 'Iron 883', 883, 'Cruiser');

-- Inserção de dados em Patio
INSERT INTO Patio VALUES (1, 'Pátio Central', 'Centro', 500.50, 50);
INSERT INTO Patio VALUES (2, 'Pátio Norte', 'Zona Norte', 350.75, 35);
INSERT INTO Patio VALUES (3, 'Pátio Sul', 'Zona Sul', 420.30, 40);
INSERT INTO Patio VALUES (4, 'Pátio Leste', 'Zona Leste', 380.25, 38);
INSERT INTO Patio VALUES (5, 'Pátio Oeste', 'Zona Oeste', 450.00, 45);

-- Inserção de dados em Sensor_iot (inicialmente com id_moto NULL)
INSERT INTO Sensor_iot VALUES (1, 'GPS', DATE '2025-05-15', 85.5, NULL);
INSERT INTO Sensor_iot VALUES (2, 'GPS', DATE '2025-05-16', 92.3, NULL);
INSERT INTO Sensor_iot VALUES (3, 'Movimento', DATE '2025-05-17', 78.1, NULL);
INSERT INTO Sensor_iot VALUES (4, 'Temperatura', DATE '2025-05-18', 65.8, NULL);
INSERT INTO Sensor_iot VALUES (5, 'GPS', DATE '2025-05-19', 89.7, NULL);

-- Inserção de dados em Moto
INSERT INTO Moto (id_moto, placa, chassi, ano_fabricacao, status, Modelo_id_modelo, id_patio, id_sensor_iot, data_atualizacao)
VALUES (1, 'ABC1234', 'CH001ABC', 2022, 'Disponível', 1, 1, 1, DATE '2025-05-15');
INSERT INTO Moto (id_moto, placa, chassi, ano_fabricacao, status, Modelo_id_modelo, id_patio, id_sensor_iot, data_atualizacao)
VALUES (2, 'DEF5678', 'CH002DEF', 2023, 'Em Uso', 2, 2, 2, DATE '2025-05-16');
INSERT INTO Moto (id_moto, placa, chassi, ano_fabricacao, status, Modelo_id_modelo, id_patio, id_sensor_iot, data_atualizacao)
VALUES (3, 'GHI9012', 'CH003GHI', 2021, 'Manutenção', 3, 3, 3, DATE '2025-05-17');
INSERT INTO Moto (id_moto, placa, chassi, ano_fabricacao, status, Modelo_id_modelo, id_patio, id_sensor_iot, data_atualizacao)
VALUES (4, 'JKL3456', 'CH004JKL', 2024, 'Disponível', 4, 4, 4, DATE '2025-05-18');
INSERT INTO Moto (id_moto, placa, chassi, ano_fabricacao, status, Modelo_id_modelo, id_patio, id_sensor_iot, data_atualizacao)
VALUES (5, 'MNO7890', 'CH005MNO', 2022, 'Em Uso', 5, 5, 5, DATE '2025-05-19');

-- Atualização dos sensores com os IDs das motos
UPDATE Sensor_iot SET id_moto = 1 WHERE id_sensor_iot = 1;
UPDATE Sensor_iot SET id_moto = 2 WHERE id_sensor_iot = 2;
UPDATE Sensor_iot SET id_moto = 3 WHERE id_sensor_iot = 3;
UPDATE Sensor_iot SET id_moto = 4 WHERE id_sensor_iot = 4;
UPDATE Sensor_iot SET id_moto = 5 WHERE id_sensor_iot = 5;

-- Inserção de dados em Historico_posicao (timestamp literal)
INSERT INTO Historico_posicao VALUES (TIMESTAMP '2025-05-15 08:00:00', 100.25, 200.35, 3.5, 'GPS', 'Estacionada', 1);
INSERT INTO Historico_posicao VALUES (TIMESTAMP '2025-05-16 09:15:00', 150.50, 250.75, 2.8, 'GPS', 'Em Movimento', 2);
INSERT INTO Historico_posicao VALUES (TIMESTAMP '2025-05-17 10:30:00', 200.75, 300.90, 1.9, 'Triangulação', 'Estacionada', 3);
INSERT INTO Historico_posicao VALUES (TIMESTAMP '2025-05-18 11:45:00', 250.30, 350.45, 4.2, 'GPS', 'Em Movimento', 4);
INSERT INTO Historico_posicao VALUES (TIMESTAMP '2025-05-19 12:00:00', 300.60, 400.80, 2.5, 'Triangulação', 'Estacionada', 5);

-- Inserção adicional para garantir mais de 5 registros na tabela Historico_posicao
INSERT INTO Historico_posicao VALUES (TIMESTAMP '2025-05-15 14:30:00', 105.30, 205.40, 3.2, 'GPS', 'Em Movimento', 1);
INSERT INTO Historico_posicao VALUES (TIMESTAMP '2025-05-16 15:45:00', 155.60, 255.80, 2.6, 'GPS', 'Estacionada', 2);

-- Inserção de dados em Alerta_evento
INSERT INTO Alerta_evento VALUES (1, 'Bateria Baixa', TIMESTAMP '2025-05-15 10:00:00', 1);
INSERT INTO Alerta_evento VALUES (2, 'Movimento Suspeito', TIMESTAMP '2025-05-16 11:30:00', 2);
INSERT INTO Alerta_evento VALUES (3, 'Sinal Perdido', TIMESTAMP '2025-05-17 13:45:00', 3);
INSERT INTO Alerta_evento VALUES (4, 'Manutenção Necessária', TIMESTAMP '2025-05-18 15:20:00', 4);
INSERT INTO Alerta_evento VALUES (5, 'Bateria Baixa', TIMESTAMP '2025-05-19 16:10:00', 5);

-- Criação da tabela Auditoria (CLOB -> text)
CREATE TABLE Auditoria_Moto (
    id_auditoria     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_banco    varchar(50),
    tipo_operacao    varchar(10),
    data_operacao    timestamp,
    valor_anterior   text,
    valor_novo       text
);

-- Trigger de auditoria em PL/pgSQL
CREATE OR REPLACE FUNCTION trg_auditoria_moto_fn()
RETURNS trigger AS $$
DECLARE
    v_valor_anterior text := NULL;
    v_valor_novo text := NULL;
    v_operacao text := NULL;
BEGIN
    -- Descobre o tipo da operação
    IF TG_OP = 'INSERT' THEN
        v_operacao := 'INSERT';
    ELSIF TG_OP = 'UPDATE' THEN
        v_operacao := 'UPDATE';
    ELSIF TG_OP = 'DELETE' THEN
        v_operacao := 'DELETE';
    END IF;

    -- Monta valores anteriores
    IF TG_OP = 'DELETE' OR TG_OP = 'UPDATE' THEN
        v_valor_anterior := format('{"id_moto": %s, "placa": "%s", "chassi": "%s", "ano_fabricacao": %s, "status": "%s"}',
                                   OLD.id_moto, COALESCE(OLD.placa, ''), COALESCE(OLD.chassi, ''), OLD.ano_fabricacao, COALESCE(OLD.status, ''));
    END IF;

    -- Monta valores novos
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        v_valor_novo := format('{"id_moto": %s, "placa": "%s", "chassi": "%s", "ano_fabricacao": %s, "status": "%s"}',
                               NEW.id_moto, COALESCE(NEW.placa, ''), COALESCE(NEW.chassi, ''), NEW.ano_fabricacao, COALESCE(NEW.status, ''));
    END IF;

    -- Grava auditoria
    INSERT INTO Auditoria_Moto (usuario_banco, tipo_operacao, data_operacao, valor_anterior, valor_novo)
    VALUES (current_user, v_operacao, now(), v_valor_anterior, v_valor_novo);

    RETURN NULL; -- AFTER trigger
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auditoria_moto
AFTER INSERT OR UPDATE OR DELETE ON Moto
FOR EACH ROW EXECUTE FUNCTION trg_auditoria_moto_fn();

-------------------------------------------------------------------------------
-- Função fn_gerar_json em PL/pgSQL
CREATE OR REPLACE FUNCTION fn_gerar_json(
    p_id integer,
    p_placa varchar,
    p_modelo varchar,
    p_fabricante varchar,
    p_patio varchar
) RETURNS text AS $$
DECLARE
    v_json text := '';
BEGIN
    IF p_id IS NULL THEN
        RETURN '{"error":"NO_DATA_FOUND","message":"Chave primária ausente ou nula"}';
    END IF;

    v_json := format('{"id_moto": %s, "placa": "%s", "modelo": "%s", "fabricante": "%s", "patio": "%s"}',
                     p_id, COALESCE(p_placa,''), COALESCE(p_modelo,''), COALESCE(p_fabricante,''), COALESCE(p_patio,''));

    RETURN v_json;
EXCEPTION
    WHEN others THEN
        RETURN format('{"error":"UNEXPECTED","message":"%s"}', replace(SQLERRM, '"', ' '));
END;
$$ LANGUAGE plpgsql;

-- Função fn_validar_senha em PL/pgSQL
CREATE OR REPLACE FUNCTION fn_validar_senha(p_senha varchar)
RETURNS varchar AS $$
DECLARE
    v_tem_numero boolean := false;
    v_tem_maiuscula boolean := false;
    i integer;
    c char;
BEGIN
    -- Validação: senha nula
    IF p_senha IS NULL THEN
        RETURN 'INVALIDA: senha nula';
    END IF;

    -- Validação: tamanho mínimo
    IF length(p_senha) < 6 THEN
        RETURN 'INVALIDA: tamanho mínimo de 6 caracteres';
    END IF;

    -- Varre cada caractere
    FOR i IN 1..length(p_senha) LOOP
        c := substr(p_senha, i, 1);
        IF c ~ '[0-9]' THEN
            v_tem_numero := true;
        END IF;
        IF c = upper(c) AND c ~ '[A-Z]' THEN
            v_tem_maiuscula := true;
        END IF;
    END LOOP;

    -- Regras de validação
    IF NOT v_tem_numero THEN
        RETURN 'INVALIDA: deve conter pelo menos um número';
    ELSIF NOT v_tem_maiuscula THEN
        RETURN 'INVALIDA: deve conter pelo menos uma letra maiúscula';
    ELSE
        RETURN 'VALIDA';
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Procedimento prc_relatorio_motos_json em PL/pgSQL (usa RAISE NOTICE ao invés do DBMS_OUTPUT)
CREATE OR REPLACE PROCEDURE prc_relatorio_motos_json()
LANGUAGE plpgsql
AS $$
DECLARE
    v_json text;
    rec RECORD;
BEGIN
    RAISE NOTICE '=== RELATÓRIO EM JSON (MOTO + MODELO + PÁTIO) ===';

    FOR rec IN (
        SELECT m.id_moto, m.placa, mo.nome_modelo, mo.fabricante, p.nome_patio
        FROM Moto m
        JOIN Modelo mo ON m.Modelo_id_modelo = mo.id_modelo
        JOIN Patio p ON m.id_patio = p.id_patio
    ) LOOP
        v_json := fn_gerar_json(rec.id_moto, rec.placa, rec.nome_modelo, rec.fabricante, rec.nome_patio);
        RAISE NOTICE '%', v_json;
    END LOOP;
END;
$$;

-- Procedimento prc_relatorio_posicoes em PL/pgSQL
CREATE OR REPLACE PROCEDURE prc_relatorio_posicoes()
LANGUAGE plpgsql
AS $$
DECLARE
    rec RECORD;
    v_status text;
    v_origem text;
    v_acuracia numeric;
    v_status_atual text := NULL;
    v_subtotal numeric := 0;
    v_totalgeral numeric := 0;
BEGIN
    RAISE NOTICE 'Status          | Origem        | Acuracia';
    RAISE NOTICE '----------------+---------------+------------';

    FOR rec IN (SELECT status_no_momento, origem_detectada, acuracia_localizacao FROM Historico_posicao ORDER BY status_no_momento, origem_detectada) LOOP
        v_status := rec.status_no_momento;
        v_origem := rec.origem_detectada;
        v_acuracia := rec.acuracia_localizacao;

        IF v_status_atual IS NOT NULL AND v_status <> v_status_atual THEN
            RAISE NOTICE '                | Sub Total     | %', to_char(v_subtotal, 'FM9999990.99');
            v_subtotal := 0;
        END IF;

        RAISE NOTICE '% | % | %', lpad(coalesce(v_status,''),16), lpad(coalesce(v_origem,''),12), to_char(v_acuracia,'FM9999990.99');

        v_subtotal := v_subtotal + coalesce(v_acuracia,0);
        v_totalgeral := v_totalgeral + coalesce(v_acuracia,0);
        v_status_atual := v_status;
    END LOOP;

    IF v_status_atual IS NOT NULL THEN
        RAISE NOTICE '                | Sub Total     | %', to_char(v_subtotal, 'FM9999990.99');
    END IF;

    RAISE NOTICE '                | Total Geral   | %', to_char(v_totalgeral, 'FM9999990.99');
END;
$$;

-------------------------------------------------------------------------------
-- Bloco anônimo 1 (relatórios) convertido para DO block
DO $$
DECLARE
    rec RECORD;
BEGIN
    RAISE NOTICE '=== RELATÓRIO 1: DADOS DAS MOTOS POR MODELO E FABRICANTE ===';
    RAISE NOTICE 'Modelo | Fabricante | Quantidade de Motos';
    RAISE NOTICE '------------------------------------------------';

    FOR rec IN (
        SELECT m.nome_modelo, m.fabricante, COUNT(mt.id_moto) as total_motos
        FROM Modelo m
        JOIN Moto mt ON m.id_modelo = mt.Modelo_id_modelo
        GROUP BY m.nome_modelo, m.fabricante
        ORDER BY total_motos DESC
    ) LOOP
        RAISE NOTICE '% | % | %', rec.nome_modelo, rec.fabricante, rec.total_motos;
    END LOOP;

    RAISE NOTICE '';
    RAISE NOTICE '=== RELATÓRIO 2: MOTOS POR PÁTIO COM CAPACIDADE ===';
    RAISE NOTICE 'Pátio | Total de Motos | Capacidade Máxima | %% Ocupação';
    RAISE NOTICE '------------------------------------------------------';

    FOR rec IN (
        SELECT p.nome_patio, COUNT(m.id_moto) as total_motos, p.capacidade_maxima,
               ROUND((COUNT(m.id_moto)::numeric / p.capacidade_maxima) * 100, 2) as percentual_ocupacao
        FROM Patio p
        LEFT JOIN Moto m ON p.id_patio = m.id_patio
        GROUP BY p.nome_patio, p.capacidade_maxima
        ORDER BY percentual_ocupacao DESC
    ) LOOP
        RAISE NOTICE '% | % | % | %', rec.nome_patio, rec.total_motos, rec.capacidade_maxima, rec.percentual_ocupacao;
    END LOOP;

    RAISE NOTICE '';
    RAISE NOTICE '=== RELATÓRIO 3: ACURÁCIA MÉDIA DE LOCALIZAÇÃO POR STATUS ===';
    RAISE NOTICE 'Status | Acurácia Média | Quantidade de Registros';
    RAISE NOTICE '------------------------------------------------';

    FOR rec IN (
        SELECT hp.status_no_momento, ROUND(AVG(hp.acuracia_localizacao)::numeric, 2) as acuracia_media,
               COUNT(*) as total_registros
        FROM Historico_posicao hp
        JOIN Moto m ON hp.id_moto = m.id_moto
        GROUP BY hp.status_no_momento
        ORDER BY acuracia_media ASC
    ) LOOP
        RAISE NOTICE '% | % | %', rec.status_no_momento, rec.acuracia_media, rec.total_registros;
    END LOOP;
END $$;

-------------------------------------------------------------------------------
-- Bloco anônimo 2 (mais relatórios)
DO $$
DECLARE
    rec RECORD;
BEGIN
    RAISE NOTICE '=== RELATÓRIO 4: BATERIA MÉDIA POR TIPO DE SENSOR ===';
    RAISE NOTICE 'Tipo de Sensor | Bateria Média (%) | Quantidade';

    FOR rec IN (
        SELECT si.tipo_sensor, ROUND(AVG(si.bateria_percentual)::numeric, 2) as bateria_media, COUNT(*) as quantidade
        FROM Sensor_iot si
        JOIN Moto m ON si.id_sensor_iot = m.id_sensor_iot
        GROUP BY si.tipo_sensor
        ORDER BY bateria_media DESC
    ) LOOP
        RAISE NOTICE '% | % | %', rec.tipo_sensor, rec.bateria_media, rec.quantidade;
    END LOOP;

    RAISE NOTICE '';
    RAISE NOTICE '=== RELATÓRIO 5: CONTAGEM DE MOTOS POR STATUS E FABRICANTE ===';

    FOR rec IN (
        SELECT m.status, mo.fabricante, COUNT(*) as total_motos
        FROM Moto m
        JOIN Modelo mo ON m.Modelo_id_modelo = mo.id_modelo
        GROUP BY m.status, mo.fabricante
        ORDER BY m.status, total_motos DESC
    ) LOOP
        RAISE NOTICE '% | % | %', rec.status, rec.fabricante, rec.total_motos;
    END LOOP;

    RAISE NOTICE '';
    RAISE NOTICE '=== RELATÓRIO 6: ALERTAS POR TIPO E FABRICANTE ===';

    FOR rec IN (
        SELECT ae.tipo_alerta, mo.fabricante, COUNT(*) as total_alertas
        FROM Alerta_evento ae
        JOIN Moto m ON ae.id_moto = m.id_moto
        JOIN Modelo mo ON m.Modelo_id_modelo = mo.id_modelo
        GROUP BY ae.tipo_alerta, mo.fabricante
        ORDER BY total_alertas DESC
    ) LOOP
        RAISE NOTICE '% | % | %', rec.tipo_alerta, rec.fabricante, rec.total_alertas;
    END LOOP;
END $$;

-------------------------------------------------------------------------------
-- Bloco 3 (LAG/LEAD)
DO $$
DECLARE
    rec RECORD;
BEGIN
  RAISE NOTICE '=== RELATÓRIO DE ANOS DE FABRICAÇÃO DAS MOTOS (COM LAG E LEAD) ===';
  RAISE NOTICE 'ID Moto | Placa | Ano Anterior | Ano Atual | Próximo Ano';
  RAISE NOTICE '-------------------------------------------------------';

  FOR rec IN (
    SELECT id_moto, placa, ano_fabricacao,
           LAG(ano_fabricacao) OVER (ORDER BY ano_fabricacao) AS ano_anterior,
           LEAD(ano_fabricacao) OVER (ORDER BY ano_fabricacao) AS ano_proximo
    FROM Moto
    ORDER BY ano_fabricacao
  ) LOOP
    RAISE NOTICE '% | % | % | % | %', rec.id_moto, rec.placa,
                 COALESCE(rec.ano_anterior::text, 'Vazio'), rec.ano_fabricacao, COALESCE(rec.ano_proximo::text, 'Vazio');
  END LOOP;
END $$;

-------------------------------------------------------------------------------
-- TESTES
-------------------------------------------------------------------------------
-- No PostgreSQL não existe SET SERVEROUTPUT ON; usamos RAISE NOTICE dentro de DO blocks ou funções.

-- Teste Função 1 (fn_gerar_json)
DO $$
DECLARE
    v_json text;
BEGIN
    v_json := fn_gerar_json(1, 'ABC1234', 'CG 160', 'Honda', 'Pátio Central');
    RAISE NOTICE '%', v_json;
END $$;

-- Teste Função 1 com exceção
DO $$
DECLARE
    v_json text;
BEGIN
    v_json := fn_gerar_json(NULL, 'ABC1234', 'CG 160', 'Honda', 'Pátio Central');
    RAISE NOTICE '%', v_json;
END $$;

-- Teste Função 2 (fn_validar_senha)
DO $$
DECLARE
    v_result varchar(100);
BEGIN
    v_result := fn_validar_senha('Abc123');
    RAISE NOTICE '%', v_result;
END $$;

-- Teste Função 2 com exceção
DO $$
DECLARE
    v_result varchar(100);
BEGIN
    v_result := fn_validar_senha(NULL);
    RAISE NOTICE '%', v_result;
END $$;

-- Teste Procedimento 1
CALL prc_relatorio_motos_json();

-- Teste Procedimento 2
CALL prc_relatorio_posicoes();

-- Teste Trigger (INSERT, UPDATE, DELETE)
INSERT INTO Moto (id_moto, placa, chassi, ano_fabricacao, status, Modelo_id_modelo, id_patio, id_sensor_iot, data_atualizacao)
VALUES (99, 'TEST9999', 'CHTEST999', 2025, 'Disponível', 1, 1, 1, now());

UPDATE Moto SET status = 'Manutenção' WHERE id_moto = 99;

DELETE FROM Moto WHERE id_moto = 99;

-- visualizar auditoria
SELECT * FROM Auditoria_Moto;

SELECT * FROM flyway_schema_history;
