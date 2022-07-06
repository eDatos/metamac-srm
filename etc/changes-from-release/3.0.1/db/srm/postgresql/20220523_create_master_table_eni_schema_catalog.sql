-- --------------------------------------------------------------------------------------------------
-- EDATOS-3626 - [srm] Añadir mapeo de los temas con los propuestos por ENI-NT
-- Se crea tabla maestra con los valores indicados en el boe para la taxonomía de sectores primarios.
-- Se añade a la tabla existente TB_M_CATEGORIES la nueva columna ENI_SCHEMA_FK
-- --------------------------------------------------------------------------------------------------

CREATE TABLE TB_LIS_ENI_SCHEMA_CATALOGS (
  ID BIGINT NOT NULL,
  IDENTIFIER VARCHAR(255) NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  DESCRIPTION BIGINT NOT NULL
);

ALTER TABLE TB_LIS_ENI_SCHEMA_CATALOGS ADD CONSTRAINT PK_TB_LIS_ENI_SCHEMA_CATALOGS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_LIS_ENI_SCHEMA_CATALOGS
    ADD CONSTRAINT UQ_TB_LIS_ENI_SCHEMA_CATALOGS UNIQUE (UUID)
;

ALTER TABLE TB_LIS_ENI_SCHEMA_CATALOGS ADD CONSTRAINT FK_TB_LIS_ENI_SCHEMA_CATALOGS_DESCRIPTION
	FOREIGN KEY (DESCRIPTION) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

commit;
