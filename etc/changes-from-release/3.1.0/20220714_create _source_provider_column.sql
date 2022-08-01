-- --------------------------------------------------------------------------------------------------
-- EDATOS-3677 - [srm] Gestión de codelist
-- Se  crea la columna para el metadato sourceProvider que será del tipo organización no obligatorio
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_M_CODELISTS_VERSIONS ADD COLUMN SOURCE_PROVIDER_FK BIGINT;

ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_SOURCE_PROVIDER_FK FOREIGN KEY (SOURCE_PROVIDER_FK) REFERENCES TB_ORGANISATIONS (ID);

commit;