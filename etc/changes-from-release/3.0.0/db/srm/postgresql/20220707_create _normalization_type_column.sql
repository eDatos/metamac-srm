-- --------------------------------------------------------------------------------------------------
-- EDATOS-3677 - [srm] Gestión de codelist no normalizados
-- Se  crea la columna para el metadato normalizationType que será obligatorio con valores
--      - STANDARDIZED
--	  - NOT_STANDARDIZED
-- Por defecto tendrá el valor "STANDARDIZED"
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_M_CODELISTS_VERSIONS ADD COLUMN NORMALIZATION_TYPE VARCHAR(255) NOT NULL DEFAULT 'STANDARDIZED';

commit;