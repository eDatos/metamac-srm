-- --------------------------------------------------------------------------------------------------
-- EDATOS-3626 - [srm] Añadir mapeo de los temas con los propuestos por ENI-NT
-- Se añade foreign key en tb_m_categories al nuevo campo ENI_SCHEMA_FK a la tabla maestra TB_LIS_ENI_SCHEMA_CATALOGS
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_M_CATEGORIES ADD CONSTRAINT FK_TB_M_CATEGORIES_ENI_SCHEMA_FK
	FOREIGN KEY (ENI_SCHEMA_FK) REFERENCES TB_LIS_ENI_SCHEMA_CATALOGS (ID)
;

ALTER TABLE TB_M_CATEGORIES add column ENI_SCHEMA_FK BIGINT;

commit;