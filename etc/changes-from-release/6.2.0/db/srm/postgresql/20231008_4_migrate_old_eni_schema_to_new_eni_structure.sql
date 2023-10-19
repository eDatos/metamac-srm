-- --------------------------------------------------------------------------------------------------
-- EDATOS-4190 - Permitir enlazar más de un tema ENI a cada tema
-- 
-- Migrar el campo antiguo con el esquema eni que está en la tabla tb_M_categories con el nombre deprecated_eni_schema_fk a la nueva tabla multivaluada TB_CAT_ENI_SCHEMA
-- --------------------------------------------------------------------------------------------------

-- Paso 1. Obtener inserts a la nueva tabla de esquemas eni por tema.
select 'INSERT INTO TB_CAT_ENI_SCHEMA(ENI_SCHEMA, TB_M_CATEGORIES) VALUES(' || deprecated_eni_schema_fk || ', ' || tb_categories || ');' from tb_m_categories where deprecated_eni_schema_fk is not null;
/* Ejemplo de salida
INSERT INTO TB_CAT_ENI_SCHEMA(ENI_SCHEMA_FK, TB_CATEGORIES) VALUES(1, 53001);
INSERT INTO TB_CAT_ENI_SCHEMA(ENI_SCHEMA_FK, TB_CATEGORIES) VALUES(2, 52901);
*/

-- Paso 2. Ejecutar los inserts obtenidos en el apartado anterior.



commit;