-- --------------------------------------------------------------------------------------------------
-- EDATOS-4153 Revisión y eliminación de índices de BBDD duplicados en las aplicaciones de eDatos
-- --------------------------------------------------------------------------------------------------
-- La detección de índices duplicados se ha llevado a cabo ejecutando la siguiente consulta
-- Es necesario la revisión de los resultados de la misma antes de determinar que un índice está duplicado
--
-- select indrelid::regclass, array_agg(indexrelid::regclass) from pg_index group by indrelid, indkey having COUNT(*) > 1;
-- --------------------------------------------------------------------------------------------------

-- Tabla: tb_m_codelists_versions
-- Columna: tb_codelists_versions
-- Indices duplicados: ix_tb_m_codelists_v_fk y tb_m_codelists_versions_pkey
-- Se mantiene el índice: tb_m_codelists_versions_pkey
DROP INDEX IF EXISTS ix_tb_m_codelists_v_fk;

-- Tabla: tb_m_concept_schemes_versions
-- Columna: tb_concept_schemes_versions
-- Indices duplicados: ix_tb_m_concepts_schemes_v_fk y tb_m_concept_schemes_versions_pkey
-- Se mantiene el índice: tb_m_concept_schemes_versions_pkey
DROP INDEX IF EXISTS ix_tb_m_concepts_schemes_v_fk;

-- Tabla: tb_m_cat_schemes_versions
-- Columna: tb_cat_schemes_versions
-- Indices duplicados: ix_tb_m_cat_schemes_v_fk y tb_m_cat_schemes_versions_pkey
-- Se mantiene el índice: tb_m_cat_schemes_versions_pkey
DROP INDEX IF EXISTS ix_tb_m_cat_schemes_v_fk;

-- Tabla: tb_m_organisations
-- Columna: tb_organisations
-- Indices duplicados: ix_tb_m_organisations_fk y tb_m_organisations_pkey
-- Se mantiene el índice: tb_m_organisations_pkey
DROP INDEX IF EXISTS ix_tb_m_organisations_fk;

commit;







