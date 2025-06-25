-- --------------------------------------------------------------------------------------------------
-- EDATOS-4957 - [srm] Añadir órdenes de visualización a los esquemas de conceptos.
--  El campo que se usaba hasta ahora para la ordenación de los códigos de cada esquema de conceptos, se depreca.

-- Se crea EDATOS-5096 para borrar este campo una vez se ponga la tarea en producción.

-- --------------------------------------------------------------------------------------------------

 ALTER TABLE TB_M_CONCEPTS RENAME COLUMN order_value TO deprecated_order_value;

commit;