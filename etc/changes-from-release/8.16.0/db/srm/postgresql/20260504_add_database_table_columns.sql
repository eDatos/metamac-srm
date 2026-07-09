-- --------------------------------------------------------------------------------------------------
-- EDATOS-5569 - [srm] Modificar la API del SRM para incluir cabeceras adecuadas de Cache / LastModified / E-tag

-- Añadir columnas a tablas de base de datos
-- --------------------------------------------------------------------------------------------------

-- Campo dedicado para trackear la última modificación de elementos de variable
-- Se actualiza en create/update/delete (y en importaciones TSV/Shape) de VariableElement
-- Se usa en findMaxLastUpdatedVariableElement para la caché HTTP de la API externa
ALTER TABLE TB_M_VARIABLES ADD COLUMN ELEMENTS_LAST_UPDATED TIMESTAMP;
ALTER TABLE TB_M_VARIABLES ADD COLUMN ELEMENTS_LAST_UPDATED_TZ VARCHAR(50);

commit;