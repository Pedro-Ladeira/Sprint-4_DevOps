--Drop table caso seja necessario

 DROP TABLE Historico_posicao CASCADE CONSTRAINTS;
DROP TABLE Alerta_evento CASCADE CONSTRAINTS;
DROP TABLE Sensor_iot CASCADE CONSTRAINTS;
DROP TABLE Moto CASCADE CONSTRAINTS;
DROP TABLE Usuario_sistema CASCADE CONSTRAINTS;
DROP TABLE Modelo CASCADE CONSTRAINTS;
DROP TABLE Patio CASCADE CONSTRAINTS;

desc modelo


select * from moto;

-- Criação das tabelas baseadas no diagrama ER
CREATE TABLE Usuario_sistema (
    id_usuario INTEGER PRIMARY KEY,
    nome VARCHAR2(50),
    email VARCHAR2(100),
    senha VARCHAR2(20)
);

CREATE TABLE Modelo (
    id_modelo INTEGER PRIMARY KEY,
    fabricante VARCHAR2(50),
    nome_modelo VARCHAR2(50),
    cilindrada INTEGER,
    tipo VARCHAR2(30)
);

CREATE TABLE Patio (
    id_patio INTEGER PRIMARY KEY,
    nome_patio VARCHAR2(50),
    localizacao_patio VARCHAR2(50),
    area_total NUMBER(10,2),
    capacidade_maxima INTEGER
);

CREATE TABLE Sensor_iot (
    id_sensor_iot INTEGER PRIMARY KEY,
    tipo_sensor VARCHAR2(20),
    data_transmissao DATE,
    bateria_percentual NUMBER(5,2),
    id_moto INTEGER
);

CREATE TABLE Moto (
    id_moto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    placa VARCHAR2(10),
    chassi VARCHAR2(50),
    ano_fabricacao INTEGER,
    status VARCHAR2(20),
    Modelo_id_modelo INTEGER,
    id_patio INTEGER,
    id_sensor_iot INTEGER,
    data_atualizacao DATE,
    CONSTRAINT fk_modelo FOREIGN KEY (Modelo_id_modelo) REFERENCES Modelo(id_modelo),
    CONSTRAINT fk_patio FOREIGN KEY (id_patio) REFERENCES Patio(id_patio),
    CONSTRAINT fk_sensor_iot FOREIGN KEY (id_sensor_iot) REFERENCES Sensor_iot(id_sensor_iot)
);

-- Adiciona chave estrangeira ao Sensor_iot após criação da tabela Moto
ALTER TABLE Sensor_iot
ADD CONSTRAINT fk_moto_sensor FOREIGN KEY (id_moto) REFERENCES Moto(id_moto);

CREATE TABLE Historico_posicao (
    data_atualizacao DATE,
    posicao_x NUMBER(10,2),
    posicao_y NUMBER(10,2),
    acuracia_localizacao NUMBER(5,2),
    origem_detectada VARCHAR2(20),
    status_no_momento VARCHAR2(20),
    id_moto INTEGER,
    CONSTRAINT fk_moto_historico FOREIGN KEY (id_moto) REFERENCES Moto(id_moto),
    CONSTRAINT pk_historico PRIMARY KEY (data_atualizacao, id_moto)
);

CREATE TABLE Alerta_evento (
    id_alerta INTEGER PRIMARY KEY,
    tipo_alerta VARCHAR2(50),
    data_geracao DATE,
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
INSERT INTO Sensor_iot VALUES (1, 'GPS', TO_DATE('2025-05-15', 'YYYY-MM-DD'), 85.5, NULL);
INSERT INTO Sensor_iot VALUES (2, 'GPS', TO_DATE('2025-05-16', 'YYYY-MM-DD'), 92.3, NULL);
INSERT INTO Sensor_iot VALUES (3, 'Movimento', TO_DATE('2025-05-17', 'YYYY-MM-DD'), 78.1, NULL);
INSERT INTO Sensor_iot VALUES (4, 'Temperatura', TO_DATE('2025-05-18', 'YYYY-MM-DD'), 65.8, NULL);
INSERT INTO Sensor_iot VALUES (5, 'GPS', TO_DATE('2025-05-19', 'YYYY-MM-DD'), 89.7, NULL);

-- Inserção de dados em Moto
INSERT INTO Moto VALUES (1, 'ABC1234', 'CH001ABC', 2022, 'Disponível', 1, 1, 1, TO_DATE('2025-05-15', 'YYYY-MM-DD'));
INSERT INTO Moto VALUES (2, 'DEF5678', 'CH002DEF', 2023, 'Em Uso', 2, 2, 2, TO_DATE('2025-05-16', 'YYYY-MM-DD'));
INSERT INTO Moto VALUES (3, 'GHI9012', 'CH003GHI', 2021, 'Manutenção', 3, 3, 3, TO_DATE('2025-05-17', 'YYYY-MM-DD'));
INSERT INTO Moto VALUES (4, 'JKL3456', 'CH004JKL', 2024, 'Disponível', 4, 4, 4, TO_DATE('2025-05-18', 'YYYY-MM-DD'));
INSERT INTO Moto VALUES (5, 'MNO7890', 'CH005MNO', 2022, 'Em Uso', 5, 5, 5, TO_DATE('2025-05-19', 'YYYY-MM-DD'));

-- Atualização dos sensores com os IDs das motos
UPDATE Sensor_iot SET id_moto = 1 WHERE id_sensor_iot = 1;
UPDATE Sensor_iot SET id_moto = 2 WHERE id_sensor_iot = 2;
UPDATE Sensor_iot SET id_moto = 3 WHERE id_sensor_iot = 3;
UPDATE Sensor_iot SET id_moto = 4 WHERE id_sensor_iot = 4;
UPDATE Sensor_iot SET id_moto = 5 WHERE id_sensor_iot = 5;

-- Inserção de dados em Historico_posicao
INSERT INTO Historico_posicao VALUES (TO_DATE('2025-05-15 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 100.25, 200.35, 3.5, 'GPS', 'Estacionada', 1);
INSERT INTO Historico_posicao VALUES (TO_DATE('2025-05-16 09:15:00', 'YYYY-MM-DD HH24:MI:SS'), 150.50, 250.75, 2.8, 'GPS', 'Em Movimento', 2);
INSERT INTO Historico_posicao VALUES (TO_DATE('2025-05-17 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), 200.75, 300.90, 1.9, 'Triangulação', 'Estacionada', 3);
INSERT INTO Historico_posicao VALUES (TO_DATE('2025-05-18 11:45:00', 'YYYY-MM-DD HH24:MI:SS'), 250.30, 350.45, 4.2, 'GPS', 'Em Movimento', 4);
INSERT INTO Historico_posicao VALUES (TO_DATE('2025-05-19 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 300.60, 400.80, 2.5, 'Triangulação', 'Estacionada', 5);

-- Inserção adicional para garantir mais de 5 registros na tabela Historico_posicao
INSERT INTO Historico_posicao VALUES (TO_DATE('2025-05-15 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 105.30, 205.40, 3.2, 'GPS', 'Em Movimento', 1);
INSERT INTO Historico_posicao VALUES (TO_DATE('2025-05-16 15:45:00', 'YYYY-MM-DD HH24:MI:SS'), 155.60, 255.80, 2.6, 'GPS', 'Estacionada', 2);

-- Inserção de dados em Alerta_evento
INSERT INTO Alerta_evento VALUES (1, 'Bateria Baixa', TO_DATE('2025-05-15 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1);
INSERT INTO Alerta_evento VALUES (2, 'Movimento Suspeito', TO_DATE('2025-05-16 11:30:00', 'YYYY-MM-DD HH24:MI:SS'), 2);
INSERT INTO Alerta_evento VALUES (3, 'Sinal Perdido', TO_DATE('2025-05-17 13:45:00', 'YYYY-MM-DD HH24:MI:SS'), 3);
INSERT INTO Alerta_evento VALUES (4, 'Manutenção Necessária', TO_DATE('2025-05-18 15:20:00', 'YYYY-MM-DD HH24:MI:SS'), 4);
INSERT INTO Alerta_evento VALUES (5, 'Bateria Baixa', TO_DATE('2025-05-19 16:10:00', 'YYYY-MM-DD HH24:MI:SS'), 5);

--Criação da tabela Auditoria
CREATE TABLE Auditoria_Moto (
    id_auditoria     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_banco    VARCHAR2(50),
    tipo_operacao    VARCHAR2(10),
    data_operacao    DATE,
    valor_anterior   CLOB,
    valor_novo       CLOB
);

--Criação do trigger Auditoria
CREATE OR REPLACE TRIGGER trg_auditoria_moto
AFTER INSERT OR UPDATE OR DELETE ON Moto
FOR EACH ROW
DECLARE
    v_valor_anterior CLOB;
    v_valor_novo     CLOB;
    v_operacao       VARCHAR2(10);
BEGIN
    -- Descobre o tipo da operação
    IF INSERTING THEN
        v_operacao := 'INSERT';
    ELSIF UPDATING THEN
        v_operacao := 'UPDATE';
    ELSIF DELETING THEN
        v_operacao := 'DELETE';
    END IF;

    -- Monta valores anteriores
    IF DELETING OR UPDATING THEN
        v_valor_anterior := '{' ||
            '"id_moto": ' || :OLD.id_moto || ',' ||
            '"placa": "' || :OLD.placa || '",' ||
            '"chassi": "' || :OLD.chassi || '",' ||
            '"ano_fabricacao": ' || :OLD.ano_fabricacao || ',' ||
            '"status": "' || :OLD.status || '"}';
    END IF;

    -- Monta valores novos
    IF INSERTING OR UPDATING THEN
        v_valor_novo := '{' ||
            '"id_moto": ' || :NEW.id_moto || ',' ||
            '"placa": "' || :NEW.placa || '",' ||
            '"chassi": "' || :NEW.chassi || '",' ||
            '"ano_fabricacao": ' || :NEW.ano_fabricacao || ',' ||
            '"status": "' || :NEW.status || '"}';
    END IF;

    -- Grava auditoria
    INSERT INTO Auditoria_Moto (usuario_banco, tipo_operacao, data_operacao, valor_anterior, valor_novo)
    VALUES (USER, v_operacao, SYSDATE, v_valor_anterior, v_valor_novo);
END;
/

-------------------------------------------------------------------------------
--Criação da função 1
CREATE OR REPLACE FUNCTION fn_gerar_json (
    p_id          IN INTEGER,
    p_placa       IN VARCHAR2,
    p_modelo      IN VARCHAR2,
    p_fabricante  IN VARCHAR2,
    p_patio       IN VARCHAR2
) RETURN CLOB IS
    v_json CLOB := EMPTY_CLOB();
BEGIN
    IF p_id IS NULL THEN
        RAISE NO_DATA_FOUND;
    END IF;

    v_json := '{' ||
              '"id_moto": ' || TO_CHAR(p_id) || ',' ||
              '"placa": "' || NVL(p_placa, '') || '",' ||
              '"modelo": "' || NVL(p_modelo, '') || '",' ||
              '"fabricante": "' || NVL(p_fabricante, '') || '",' ||
              '"patio": "' || NVL(p_patio, '') || '"}';

    RETURN v_json;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN '{"error":"NO_DATA_FOUND","message":"Chave primária ausente ou nula"}';
    WHEN VALUE_ERROR THEN
        RETURN '{"error":"VALUE_ERROR","message":"Erro de conversão de valor"}';
    WHEN OTHERS THEN
        RETURN '{"error":"UNEXPECTED","message":"' || REPLACE(SQLERRM, '"', ' ') || '"}';
END;
/

--Criação da função 2
CREATE OR REPLACE FUNCTION fn_validar_senha (
    p_senha IN VARCHAR2
) RETURN VARCHAR2 IS
    v_tem_numero     BOOLEAN := FALSE;
    v_tem_maiuscula  BOOLEAN := FALSE;
BEGIN
    -- Validação: senha nula
    IF p_senha IS NULL THEN
        RAISE NO_DATA_FOUND;
    END IF;

    -- Validação: tamanho mínimo
    IF LENGTH(p_senha) < 6 THEN
        RETURN 'INVALIDA: tamanho mínimo de 6 caracteres';
    END IF;

    -- Varre cada caractere
    FOR i IN 1 .. LENGTH(p_senha) LOOP
        IF SUBSTR(p_senha, i, 1) BETWEEN '0' AND '9' THEN
            v_tem_numero := TRUE;
        END IF;

        IF SUBSTR(p_senha, i, 1) = UPPER(SUBSTR(p_senha, i, 1))
           AND SUBSTR(p_senha, i, 1) BETWEEN 'A' AND 'Z' THEN
            v_tem_maiuscula := TRUE;
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

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'INVALIDA: senha nula';
    WHEN VALUE_ERROR THEN
        RETURN 'INVALIDA: erro de conversão de dados';
    WHEN OTHERS THEN
        RETURN 'INVALIDA: erro inesperado -> ' || REPLACE(SQLERRM, '"', ' ');
END;
/


--Criação do Procedimento 1
CREATE OR REPLACE PROCEDURE prc_relatorio_motos_json IS
    v_json CLOB;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO EM JSON (MOTO + MODELO + PÁTIO) ===');

    FOR rec IN (
        SELECT m.id_moto, m.placa, mo.nome_modelo, mo.fabricante, p.nome_patio
        FROM Moto m
        JOIN Modelo mo ON m.Modelo_id_modelo = mo.id_modelo
        JOIN Patio p ON m.id_patio = p.id_patio
    ) LOOP
        v_json := fn_gerar_json(rec.id_moto, rec.placa, rec.nome_modelo, rec.fabricante, rec.nome_patio);
        DBMS_OUTPUT.PUT_LINE(v_json);
    END LOOP;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Erro: Nenhum dado encontrado.');
    WHEN TOO_MANY_ROWS THEN
        DBMS_OUTPUT.PUT_LINE('Erro: Consulta retornou mais de uma linha.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Erro inesperado: ' || SQLERRM);
END;
/

--Criação do Procedimento 2
CREATE OR REPLACE PROCEDURE prc_relatorio_posicoes IS
    CURSOR c_hist IS
        SELECT status_no_momento, origem_detectada, acuracia_localizacao
        FROM Historico_posicao
        ORDER BY status_no_momento, origem_detectada;

    v_status     Historico_posicao.status_no_momento%TYPE;
    v_origem     Historico_posicao.origem_detectada%TYPE;
    v_acuracia   Historico_posicao.acuracia_localizacao%TYPE;

    v_status_atual  Historico_posicao.status_no_momento%TYPE := NULL;
    v_subtotal      NUMBER := 0;
    v_totalgeral    NUMBER := 0;
BEGIN
    DBMS_OUTPUT.PUT_LINE('Status          | Origem        | Acuracia');
    DBMS_OUTPUT.PUT_LINE('----------------+---------------+------------');

    OPEN c_hist;
    LOOP
        FETCH c_hist INTO v_status, v_origem, v_acuracia;
        EXIT WHEN c_hist%NOTFOUND;

        -- Quando muda o status, exibe subtotal
        IF v_status_atual IS NOT NULL AND v_status <> v_status_atual THEN
            DBMS_OUTPUT.PUT_LINE(LPAD(' ', 16) || ' | ' || LPAD('Sub Total', 12) || ' | ' ||
                                 TO_CHAR(v_subtotal, '9999990.99'));
            v_subtotal := 0;
        END IF;

        -- Linha detalhada
        DBMS_OUTPUT.PUT_LINE(
            RPAD(v_status, 16) || ' | ' ||
            RPAD(v_origem, 12) || ' | ' ||
            TO_CHAR(v_acuracia, '9999990.99')
        );

        -- Atualiza somatórios
        v_subtotal   := v_subtotal + v_acuracia;
        v_totalgeral := v_totalgeral + v_acuracia;
        v_status_atual := v_status;
    END LOOP;

    -- Subtotal final
    IF v_status_atual IS NOT NULL THEN
        DBMS_OUTPUT.PUT_LINE(LPAD(' ', 16) || ' | ' || LPAD('Sub Total', 12) || ' | ' ||
                             TO_CHAR(v_subtotal, '9999990.99'));
    END IF;

    -- Total geral
    DBMS_OUTPUT.PUT_LINE(LPAD(' ', 16) || ' | ' || LPAD('Total Geral', 12) || ' | ' ||
                         TO_CHAR(v_totalgeral, '9999990.99'));

    CLOSE c_hist;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Erro: Nenhum dado encontrado na tabela Historico_posicao.');
    WHEN VALUE_ERROR THEN
        DBMS_OUTPUT.PUT_LINE('Erro: Valor inválido na acurácia.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Erro inesperado: ' || SQLERRM);
END;
/


-------------------------------------------------------------------------------
-- Bloco 1 anônimo com 3 consultas JOIN com GROUP BY e ORDER BY
DECLARE
    v_modelo_nome VARCHAR2(50);
    v_fabricante VARCHAR2(50);
    v_count NUMBER;
    v_patio_nome VARCHAR2(50);
    v_total_motos NUMBER;
    v_acuracia_media NUMBER;
    v_tipo_alerta VARCHAR2(50);
    v_total_alertas NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO 1: DADOS DAS MOTOS POR MODELO E FABRICANTE ===');
    DBMS_OUTPUT.PUT_LINE('Modelo | Fabricante | Quantidade de Motos');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------');

    -- Consulta 1: Contagem de motos por modelo e fabricante
    FOR rec IN (
        SELECT m.nome_modelo, m.fabricante, COUNT(mt.id_moto) as total_motos
        FROM Modelo m
        JOIN Moto mt ON m.id_modelo = mt.Modelo_id_modelo
        GROUP BY m.nome_modelo, m.fabricante
        ORDER BY total_motos DESC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(rec.nome_modelo || ' | ' || rec.fabricante || ' | ' || rec.total_motos);
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO 2: MOTOS POR PÁTIO COM CAPACIDADE ===');
    DBMS_OUTPUT.PUT_LINE('Pátio | Total de Motos | Capacidade Máxima | % Ocupação');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------');

    -- Consulta 2: Contagem de motos por pátio com informações de capacidade
    FOR rec IN (
        SELECT p.nome_patio, COUNT(m.id_moto) as total_motos, p.capacidade_maxima,
               ROUND((COUNT(m.id_moto) / p.capacidade_maxima) * 100, 2) as percentual_ocupacao
        FROM Patio p
        LEFT JOIN Moto m ON p.id_patio = m.id_patio
        GROUP BY p.nome_patio, p.capacidade_maxima
        ORDER BY percentual_ocupacao DESC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(rec.nome_patio || ' | ' || rec.total_motos || ' | ' ||
                           rec.capacidade_maxima || ' | ' || rec.percentual_ocupacao || '%');
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO 3: ACURÁCIA MÉDIA DE LOCALIZAÇÃO POR STATUS ===');
    DBMS_OUTPUT.PUT_LINE('Status | Acurácia Média | Quantidade de Registros');
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------');

    -- Consulta 3: Acurácia média de localização por status
    FOR rec IN (
        SELECT hp.status_no_momento, ROUND(AVG(hp.acuracia_localizacao), 2) as acuracia_media,
               COUNT(*) as total_registros
        FROM Historico_posicao hp
        JOIN Moto m ON hp.id_moto = m.id_moto
        GROUP BY hp.status_no_momento
        ORDER BY acuracia_media ASC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(rec.status_no_momento || ' | ' || rec.acuracia_media || ' | ' || rec.total_registros);
    END LOOP;
END;
/

-------------------------------------------------------------------------------

-- Bloco 2 anônimo com mais 3 consultas JOIN com GROUP BY e ORDER BY
DECLARE
    v_tipo_sensor VARCHAR2(20);
    v_bateria_media NUMBER;
    v_status VARCHAR2(20);
    v_count NUMBER;
    v_fabricante VARCHAR2(50);
    v_tipo VARCHAR2(30);
    v_total_alertas NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO 4: BATERIA MÉDIA POR TIPO DE SENSOR ===');
    DBMS_OUTPUT.PUT_LINE('Tipo de Sensor | Bateria Média (%) | Quantidade');
    DBMS_OUTPUT.PUT_LINE('-------------------------------------------');

    -- Consulta 4: Bateria média por tipo de sensor
    FOR rec IN (
        SELECT si.tipo_sensor, ROUND(AVG(si.bateria_percentual), 2) as bateria_media, COUNT(*) as quantidade
        FROM Sensor_iot si
        JOIN Moto m ON si.id_sensor_iot = m.id_sensor_iot
        GROUP BY si.tipo_sensor
        ORDER BY bateria_media DESC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(rec.tipo_sensor || ' | ' || rec.bateria_media || ' | ' || rec.quantidade);
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO 5: CONTAGEM DE MOTOS POR STATUS E FABRICANTE ===');
    DBMS_OUTPUT.PUT_LINE('Status | Fabricante | Total de Motos');
    DBMS_OUTPUT.PUT_LINE('----------------------------------');

    -- Consulta 5: Contagem de motos por status e fabricante
    FOR rec IN (
        SELECT m.status, mo.fabricante, COUNT(*) as total_motos
        FROM Moto m
        JOIN Modelo mo ON m.Modelo_id_modelo = mo.id_modelo
        GROUP BY m.status, mo.fabricante
        ORDER BY m.status, total_motos DESC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(rec.status || ' | ' || rec.fabricante || ' | ' || rec.total_motos);
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO 6: ALERTAS POR TIPO E FABRICANTE ===');
    DBMS_OUTPUT.PUT_LINE('Tipo de Alerta | Fabricante | Quantidade de Alertas');
    DBMS_OUTPUT.PUT_LINE('----------------------------------------------');

    -- Consulta 6: Contagem de alertas por tipo e fabricante da moto
    FOR rec IN (
        SELECT ae.tipo_alerta, mo.fabricante, COUNT(*) as total_alertas
        FROM Alerta_evento ae
        JOIN Moto m ON ae.id_moto = m.id_moto
        JOIN Modelo mo ON m.Modelo_id_modelo = mo.id_modelo
        GROUP BY ae.tipo_alerta, mo.fabricante
        ORDER BY total_alertas DESC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(rec.tipo_alerta || ' | ' || rec.fabricante || ' | ' || rec.total_alertas);
    END LOOP;
END;
/

-------------------------------------------------------------------------------

-- Bloco 3 que mostra o valor atual, anterior e próximo de uma coluna usando funções analíticas LAG e LEAD
DECLARE
BEGIN
  DBMS_OUTPUT.PUT_LINE('=== RELATÓRIO DE ANOS DE FABRICAÇÃO DAS MOTOS (COM LAG E LEAD) ===');
  DBMS_OUTPUT.PUT_LINE('ID Moto | Placa | Ano Anterior | Ano Atual | Próximo Ano');
  DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------');

  FOR rec IN (
    SELECT id_moto, placa, ano_fabricacao,
           LAG(ano_fabricacao) OVER (ORDER BY ano_fabricacao) AS ano_anterior,
           LEAD(ano_fabricacao) OVER (ORDER BY ano_fabricacao) AS ano_proximo
    FROM Moto
    ORDER BY ano_fabricacao
  ) LOOP
    DBMS_OUTPUT.PUT_LINE(
      rec.id_moto || ' | ' ||
      rec.placa || ' | ' ||
      NVL(TO_CHAR(rec.ano_anterior), 'Vazio') || ' | ' ||
      rec.ano_fabricacao || ' | ' ||
      NVL(TO_CHAR(rec.ano_proximo), 'Vazio')
    );
  END LOOP;
END;
/

-------------------------------------------------------------------------------
--TESTES
-------------------------------------------------------------------------------
SET SERVEROUTPUT ON;
-- Teste Função 1 (fn_gerar_json)
DECLARE
    v_json CLOB;
BEGIN
    v_json := fn_gerar_json(1, 'ABC1234', 'CG 160', 'Honda', 'Pátio Central');
    DBMS_OUTPUT.PUT_LINE(v_json);
END;
/

-- Teste Função 1 com exceção
DECLARE
    v_json CLOB;
BEGIN
    v_json := fn_gerar_json(NULL, 'ABC1234', 'CG 160', 'Honda', 'Pátio Central');
    DBMS_OUTPUT.PUT_LINE(v_json);
END;
/

-- Teste Função 2 (fn_validar_senha)
DECLARE
    v_result VARCHAR2(100);
BEGIN
    v_result := fn_validar_senha('Abc123');
    DBMS_OUTPUT.PUT_LINE(v_result);
END;
/

-- Teste Função 2 com exceção
DECLARE
    v_result VARCHAR2(100);
BEGIN
    v_result := fn_validar_senha(NULL);
    DBMS_OUTPUT.PUT_LINE(v_result);
END;
/

-- Teste Procedimento 1
BEGIN
    prc_relatorio_motos_json;
END;
/

-- Teste Procedimento 2
BEGIN
    prc_relatorio_posicoes;
END;
/

-- Teste Trigger (INSERT, UPDATE, DELETE)
INSERT INTO Moto (id_moto, placa, chassi, ano_fabricacao, status, Modelo_id_modelo, id_patio, id_sensor_iot, data_atualizacao)
VALUES (99, 'TEST9999', 'CHTEST999', 2025, 'Disponível', 1, 1, 1, SYSDATE);

UPDATE Moto SET status = 'Manutenção' WHERE id_moto = 99;

DELETE FROM Moto WHERE id_moto = 99;

SELECT * FROM Auditoria_Moto;

SELECT * FROM flyway_schema_history;
