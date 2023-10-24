-- --------------------------------------------------------------------------------------------------
-- EDATOS-4190 - Permitir enlazar más de un tema ENI a cada tema
-- 
-- Añadir nueva tabla que permita especificar varios esquemas eni para un tema
-- --------------------------------------------------------------------------------------------------

CREATE TABLE TB_CAT_ENI_SCHEMA (
  ENI_SCHEMA BIGINT NOT NULL,
  TB_M_CATEGORIES BIGINT NOT NULL
);

ALTER TABLE TB_CAT_ENI_SCHEMA ADD CONSTRAINT PK_TB_CAT_ENI_SCHEMA
	PRIMARY KEY (TB_M_CATEGORIES, ENI_SCHEMA)
;

ALTER TABLE TB_CAT_ENI_SCHEMA ADD CONSTRAINT FK_TB_CAT_ENI_SCHEMA_ENI_SCHEMA_FK
	FOREIGN KEY (ENI_SCHEMA) REFERENCES TB_LIS_ENI_SCHEMA_CATALOGS (ID)
;  

ALTER TABLE TB_CAT_ENI_SCHEMA ADD CONSTRAINT FK_TB_CAT_ENI_SCHEMA_CATEGORY_FK
	FOREIGN KEY (TB_M_CATEGORIES) REFERENCES TB_M_CATEGORIES (TB_CATEGORIES)
; 

commit;