-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Actualizar propiedad stream_message_status en tabla de elementos de variable para el estado de envíos a kafka.

-- --------------------------------------------------------------------------------------------------

update  TB_M_VARIABLE_ELEMENTS
set stream_message_status  ='WITHOUT_ACTION'
where stream_message_status is null;

COMMIT;