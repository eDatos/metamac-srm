-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Añadir propiedad en el commón metadata que indique la variable de clasificación que se usará por defecto para obtener los elementos de variable asociados a territorios.

-- Añadir propiedad en el commón metadata que indique la clasificación en indicadores del tipo jsonstat o de gpe que se usará por defecto para obtener los elementos de variable asociados a códigos de territorios.

-- --------------------------------------------------------------------------------------------------
-- EDATOS-4513 - EDATOS-3513 Añadir nuevos metadatos en los elementos de tema: logo y check para que sea visible o no
-- Añadir propiedad que indique los tipos de extensiones de imágenes permitidas para la carga de logos de los elementos de tema.

-- --------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.srm.default.variable.urn','urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VR_TERRITORIO',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.srm.default.gpe.jsonstat.codelist.urn','urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_NN_VALORES_GEOGRAFICOS(~latest)',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- EDATOS-4513
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.allowed_image_extensions','svg,jpg,jpeg,png,bmp',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.image_max_size_in_kb','10',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


commit;



-- --------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.srm.default.variable.urn','urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=VR_TERRITORIO',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.srm.default.gpe.jsonstat.codelist.urn','urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_NN_VALORES_GEOGRAFICOS(~latest)',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.allowed_image_extensions','svg,jpg,jpeg,png,bmp',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


commit;

