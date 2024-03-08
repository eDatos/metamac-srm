-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4266] Borrar campo en temas no usado al permitir múltiples esquemas ENI

-- Se elimina el campo deprecado deprecated_eni_schema_fk en tarea EDATOS-4190 una vez subida release 6.2.0
-- --------------------------------------------------------------------------------------------------


-- Eliminar columna
alter table TB_M_CATEGORIES 
DROP deprecated_eni_schema_fk ;

commit;