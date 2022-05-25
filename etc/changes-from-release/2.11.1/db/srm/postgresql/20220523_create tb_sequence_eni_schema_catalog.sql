-- --------------------------------------------------------------------------------------------------
-- EDATOS-3626 - [srm] Añadir mapeo de los temas con los propuestos por ENI-NT
-- Se añade secuencia asociada a nueva tabla TB_LIS_ENI_SCHEMA_CATALOGS
-- --------------------------------------------------------------------------------------------------

INSERT INTO TB_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_VALUE) VALUES ('LIS_ENI_SCHEMA_CATALOGS', 1);

commit;