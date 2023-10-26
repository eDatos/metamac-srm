-- --------------------------------------------------------------------------------------------------
-- EDATOS-4190 - Permitir enlazar más de un tema ENI a cada tema
-- 
-- Añadir clave primaria a tabla
-- Borrar índice que ya existía sobre tb_categories ya que automáticamente se crea uno con la clave primaria.
-- --------------------------------------------------------------------------------------------------


ALTER TABLE TB_M_CATEGORIES ADD CONSTRAINT PK_TB_M_CATEGORIES
	PRIMARY KEY (TB_CATEGORIES)
;

DROP INDEX IF EXISTS ix_tb_m_categories_fk;

commit;