-- --------------------------------------------------------------------------------------------------
-- EDATOS-3772 - [e-Semántica] Eliminar proyectos de servicios web soap
-- 
-- Se añaden dos nuevos metadatos a los dsd (categorías con celdas en blanco y categorías con celdas en 0)
-- --------------------------------------------------------------------------------------------------

alter table tb_m_datastructure_versions
add column category_cell_white bool;

alter table tb_m_datastructure_versions
add column category_cell_cero bool;