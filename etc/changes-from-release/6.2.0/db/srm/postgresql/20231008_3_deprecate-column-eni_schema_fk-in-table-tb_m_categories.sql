-- --------------------------------------------------------------------------------------------------
-- EDATOS-4190 - Permitir enlazar más de un tema ENI a cada tema
-- 
-- Cambiar nombre a campo antiguo de esquema eni que ya no se va a utilizar. Se crea tarea EDATOS-4266 para ello.
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_M_CATEGORIES RENAME COLUMN eni_schema_fk TO deprecated_eni_schema_fk;

commit;