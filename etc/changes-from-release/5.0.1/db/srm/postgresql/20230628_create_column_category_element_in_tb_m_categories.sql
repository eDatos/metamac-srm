-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4150] Mapeo entre temas

-- Se crea el campo category_element_fk que referencia a tabla tb_cat_resource_elements
-- -------------------------------------------------------------------------------------------------- 
 
ALTER TABLE TB_M_CATEGORIES ADD COLUMN CATEGORY_ELEMENT_FK BIGINT;
  
ALTER TABLE TB_M_CATEGORIES ADD CONSTRAINT FK_TB_M_CATEGORIES_CATEGORY_ELEMENT_FK
	FOREIGN KEY (CATEGORY_ELEMENT_FK) REFERENCES TB_CAT_RESOURCE_ELEMENTS (TB_RESOURCE_ELEMENTS);

commit;