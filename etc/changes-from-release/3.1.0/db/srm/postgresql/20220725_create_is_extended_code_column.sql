-- --------------------------------------------------------------------------------------------------
-- EDATOS-3717 - [srm] Crear nueva columna "Código extendido" para códigos de codelist
-- Se  crea la columna "is_extended" en tb_m_codes
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_M_CODES ADD COLUMN IS_EXTENDED BOOLEAN;

commit;