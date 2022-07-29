-- --------------------------------------------------------------------------------------------------
-- EDATOS-3677 - [srm] Gestión de codelist no normalizados
-- Se  crea la columna para el metadato normalizationType que será obligatorio con valores
--      - STANDARDIZED
--	  - NOT_STANDARDIZED
-- Por defecto tendrá el valor "STANDARDIZED"
-- --------------------------------------------------------------------------------------------------

--create column
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD COLUMN NORMALIZATION_TYPE VARCHAR(255);

--set a column value to all rows
UPDATE TB_M_CODELISTS_VERSIONS SET NORMALIZATION_TYPE = 'STANDARDIZED'

--set not null and default value in column
ALTER TABLE TB_M_CODELISTS_VERSIONS ALTER COLUMN NORMALIZATION_TYPE SET NOT null;
ALTER TABLE TB_M_CODELISTS_VERSIONS ALTER COLUMN NORMALIZATION_TYPE SET DEFAULT 'STANDARDIZED'


commit;