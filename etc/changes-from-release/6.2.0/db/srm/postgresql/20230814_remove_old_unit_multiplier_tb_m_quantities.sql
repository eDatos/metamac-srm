-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4158] Eliminar de BBDD la columna old_unit_multiplier de la tabla tb_m_quantities

-- Se elimina la columna deprecado en la tarea 4059 old_unit_multiplier
-- --------------------------------------------------------------------------------------------------

-- Eliminar columna
alter table tb_m_quantities 
DROP old_unit_multiplier ;

commit;