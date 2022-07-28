-- --------------------------------------------------------------------------------------------------
-- EDATOS-3677 - [srm] Gestión de codelist no normalizados
-- Se  actualizan los metadatos de la columna normalizationType para que sea NOT NULL y con valor por defecto "STANDARDIZED"
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_M_CODELISTS_VERSIONS ALTER COLUMN NORMALIZATION_TYPE SET NOT null;
ALTER TABLE TB_M_CODELISTS_VERSIONS ALTER COLUMN NORMALIZATION_TYPE SET DEFAULT 'STANDARDIZED'

commit;