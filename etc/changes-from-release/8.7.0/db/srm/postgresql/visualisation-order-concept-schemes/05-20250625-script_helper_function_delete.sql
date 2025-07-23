-- --------------------------------------------------------------------------------------------------
-- EDATOS-4957 - [srm] Añadir órdenes de visualización a los esquemas de conceptos.
--  El campo que se usaba hasta ahora para la ordenación de los códigos de cada esquema de conceptos, se depreca.

-- --------------------------------------------------------------------------------------------------

DROP FUNCTION update_order_by_scheme_versions;
commit;