-- V004__create_functions_procedures.sql
-- Funções e procedimentos adicionais
-- Obs: auditoria já é tratada pelo SQL original

-- Função de distância
CREATE OR REPLACE FUNCTION calcular_distancia(
x1 NUMBER, y1 NUMBER, x2 NUMBER, y2 NUMBER
) RETURN NUMBER IS
BEGIN
RETURN ROUND(SQRT(POWER(x2 - x1, 2) + POWER(y2 - y1, 2)), 2);
END;
/

-- Procedimento de limpeza de dados antigos
CREATE OR REPLACE PROCEDURE limpar_dados_antigos IS
BEGIN
DELETE FROM Historico_posicao
WHERE data_atualizacao < SYSTIMESTAMP - INTERVAL '90' DAY;

```
DELETE FROM Alerta_evento
WHERE data_geracao < SYSTIMESTAMP - INTERVAL '30' DAY;

COMMIT;
```

END;
/

-- Job automático de limpeza
BEGIN
DBMS_SCHEDULER.CREATE_JOB (
job_name        => 'JOB_LIMPEZA_DADOS',
job_type        => 'PLSQL_BLOCK',
job_action      => 'BEGIN limpar_dados_antigos; END;',
start_date      => SYSTIMESTAMP,
repeat_interval => 'FREQ=WEEKLY;BYDAY=SUN;BYHOUR=2',
enabled         => TRUE
);
END;
/

COMMIT;
