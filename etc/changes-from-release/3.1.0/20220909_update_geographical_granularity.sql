-- --------------------------------------------------------------------------------------------------
-- EDATOS-3754 - Validación a la hora de importar elementos de variable desde tsv cuya granularidad geográfica no existe
--
-- Este script se compone de dos sentencias:
--
-- - La primera sentencia es una consulta que detecta aquellos elementos de variables que no tienen 
--   correctamente informado el metadato granularidad geográfica.
--
-- - La segunda sentencia es una actualización de los elementos de variables afectados estableciendo 
--   un valor por defecto.
-- -----------------------------------------------------------------------------------------------------

-- -----------------------------------------------------------------------------------------------------
-- Esta consulta extrae aquellos elementos de variable pertenecientes a una variable de tipo geográfica
-- cuya granularidad geográfica no está informada.
--
-- ATENCIÓN: La consulta sólo debería debería devolver registros en el entorno de DEMO. En caso de 
-- devolver registros en cualquier otro entorno, debe informarse al equipo de eDatos para que se tomen
-- las medidas oportunas.
-- -----------------------------------------------------------------------------------------------------
select *
from tb_m_variable_elements tmve
	left join tb_m_variables tmv on	tmve.variable_fk = tmv.id
	left join tb_annotable_artefacts taa on	tmv.nameable_artefact_fk = taa.id
where
	tmv.variable_type = 'GEOGRAPHICAL'
	and tmve.geographical_granularity_fk is null
	and taa.code != 'TERRITORIO_MUNDO';
	
-- -----------------------------------------------------------------------------------------------------
-- Esta sentencia actualiza los elementos de variable pertenecientes a una variable de tipo geográfica
-- cuya granularidad geográfica no está informada estableciendo de forma genérica el valor Otros de la 
-- clasificación ISTAC:CL_GEO_GRANULARITIES(01.006).
--
-- ATENCIÓN: Esta sentencia SÓLO HA DE EJECUTARSE EN EL ENTORNO DE DEMO dado que es el entorno donde se
-- ha detectado la incidencia tratada en esta tarea. Para cualquier realizar una adecuación	de datos
-- en cualquier otro entorno, esta consulta NO DEBE SER EJECUTADA EN SU ESTADO ACTUAL y debe ser el 
-- equipo de eDatos quien establezca el valor por defecto que debe ser informado.
-- -----------------------------------------------------------------------------------------------------
-- update tb_m_variable_elements tmve
-- set	tmve.geographical_granularity_fk = (select tc.id 
-- 										from tb_codes tc 
-- 											left join tb_annotable_artefacts taa on tc.nameable_artefact_fk = taa.id 
-- 										where 
-- 											taa.urn = 'urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_GEO_GRANULARITIES(01.006)._O')
-- where
-- 	id in (select tmve.id
-- 		   from tb_m_variable_elements tmve
-- 				left join tb_m_variables tmv on	tmve.variable_fk = tmv.id
-- 				left join tb_annotable_artefacts taa on	tmv.nameable_artefact_fk = taa.id
-- 		   where 
-- 		   		tmv.variable_type = 'GEOGRAPHICAL'
-- 		   		and tmve.geographical_granularity_fk is null
-- 		   		and taa.code != 'TERRITORIO_MUNDO');
--
-- commit;	
	