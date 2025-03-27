-- --------------------------------------------------------------------------------------------------
-- EDATOS-4926 - [srm] Permitir mostrar por defecto el código en determinadas dimensiones. Se podría poner en el DSD como metadato de configuración de previsualización.
-- Se  crea la columna "DISPLAY_CODE" que indicará si el código de la dimensión se debe visualizar por defecto en portal o no. Por defecto se pone el valor NO.
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_M_DIM_VIS_INFO ADD COLUMN DISPLAY_CODE BOOLEAN DEFAULT FALSE;

commit;