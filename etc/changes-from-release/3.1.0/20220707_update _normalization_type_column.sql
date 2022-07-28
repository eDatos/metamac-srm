-- --------------------------------------------------------------------------------------------------
-- EDATOS-3677 - [srm] Gestión de codelist no normalizados
-- Se  actualiza la columna para el metadato normalizationType  Por defecto tendrá el valor "STANDARDIZED"
-- --------------------------------------------------------------------------------------------------

UPDATE TB_M_CODELISTS_VERSIONS SET NORMALIZATION_TYPE = 'STANDARDIZED'

commit;