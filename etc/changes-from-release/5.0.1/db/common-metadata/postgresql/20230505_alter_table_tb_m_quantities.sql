-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4059] Modificar el tipo de dato del campo "Multiplicador de unidad" de número a Code

-- Se crea el campo unit_multiplier_fk de tipo integer que apunta al campo tb_codes.id
-- Se realiza la adecuación de datos de la nueva columna unit_multiplier_fk con el valor de unit_multiplier
-- Se renombra el campo existente unit_multiplier a old_unit_multiplier
-- --------------------------------------------------------------------------------------------------

-- Nuevo campo
alter table tb_m_quantities 
add column unit_multiplier_fk int8 ;

alter table tb_m_quantities 
add constraint fk_tb_m_quantities_unit_multiplier_fk
FOREIGN KEY (unit_multiplier_fk) 
REFERENCES tb_codes (id);

commit;

-- Adecuación de datos
update tb_m_quantities a
set unit_multiplier_fk = b.id
from (
	select distinct(tmq.unit_multiplier) , tc.id 
	from tb_codes tc, tb_annotable_artefacts taa, tb_m_quantities tmq 
	where tc.nameable_artefact_fk = taa.id
		and taa.urn = (
			CASE
				WHEN unit_multiplier = 1				THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).0' -- UNIDADES
				WHEN unit_multiplier = 10 				THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).1' -- DECENES
				WHEN unit_multiplier = 100 				THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).2' -- CENTENAS
				WHEN unit_multiplier = 1000 			THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).3' -- MILES
				WHEN unit_multiplier = 10000 			THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).4' -- DECENAS DE MILES
				WHEN unit_multiplier = 1000000 			THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).6' -- MILLONES
				WHEN unit_multiplier = 1000000000 		THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).9' -- BILLONES
				WHEN unit_multiplier = 1000000000000 	THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).12' -- TRILLONES
				WHEN unit_multiplier = 1000000000000000 THEN 'urn:sdmx:org.sdmx.infomodel.codelist.Code=SDMX:CL_UNIT_MULT(1.0).15' -- CUATRILLONES
			END
		)
) as b
where a.unit_multiplier = b.unit_multiplier;
commit;

-- Renombrar campo antiguo unit_multiplier a old_unit_multiplier
ALTER TABLE tb_m_quantities 
RENAME COLUMN unit_multiplier TO old_unit_multiplier;
commit;