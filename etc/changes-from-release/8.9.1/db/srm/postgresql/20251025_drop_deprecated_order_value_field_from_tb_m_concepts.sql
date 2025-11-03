-- --------------------------------------------------------------------------------------------------
-- EDATOS-5096 - [srm] Borrar campos deprecados derivados de tarea orden visualización en esquemas de conceptos

-- --------------------------------------------------------------------------------------------------
-- Eliminar la columna deprecated_order_value de la tabla tb_m_concepts
ALTER TABLE tb_m_concepts DROP COLUMN deprecated_order_value;

commit;