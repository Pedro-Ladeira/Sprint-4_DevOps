-- V001__create_inicial_tables.sql
-- Ajustes de compatibilidade no schema existente (sem recriar tabelas)

BEGIN
-- Adicionar coluna 'perfil' se não existir
DECLARE v_count INTEGER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM user_tab_columns
WHERE table_name = 'USUARIO_SISTEMA' AND column_name = 'PERFIL';

```
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE Usuario_sistema ADD (perfil VARCHAR2(20) DEFAULT ''OPERADOR'')';
    END IF;
END;

-- Adicionar coluna 'ativo' se não existir
DECLARE v_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_tab_columns
    WHERE table_name = 'USUARIO_SISTEMA' AND column_name = 'ATIVO';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE Usuario_sistema ADD (ativo NUMBER(1) DEFAULT 1)';
    END IF;
END;

-- Ajustar tamanho da senha para suportar BCrypt (255 chars)
DECLARE v_len INTEGER;
BEGIN
    SELECT data_length INTO v_len
    FROM user_tab_columns
    WHERE table_name = 'USUARIO_SISTEMA' AND column_name = 'SENHA';

    IF v_len < 255 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE Usuario_sistema MODIFY (senha VARCHAR2(255))';
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        NULL; -- coluna não existe
END;
```

END;
/

-- Criar índice para busca rápida por e-mail
DECLARE v_count INTEGER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM user_indexes
WHERE index_name = 'IDX_USUARIO_EMAIL';

```
IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_usuario_email ON Usuario_sistema(email)';
END IF;
```

END;
/
