-- --------------------------------------------------------------------------------------------------
-- EDATOS-4957 - [srm] Añadir órdenes de visualización a los esquemas de conceptos.
-- asociar al dsd el orden de visualización de un esquema de conceptos si lo tiene asociado.
-- --------------------------------------------------------------------------------------------------
ALTER TABLE TB_M_DIM_VIS_INFO
ADD COLUMN   DISPLAY_CONCEPT_SCHEME_ORDER_FK BIGINT;

ALTER TABLE TB_M_DIM_VIS_INFO ADD CONSTRAINT FK_TB_M_DIM_VIS_INFO_DISPLAY_CONCEPT_SCHEME_ORDER_FK
	FOREIGN KEY (DISPLAY_CONCEPT_SCHEME_ORDER_FK) REFERENCES TB_M_CONCEPT_SCHEME_ORDER_VIS (ID)
;

commit;