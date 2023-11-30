-- --------------------------------------------------------------------------------------------------
-- EDATOS-4197 - Añadir metadato unidad de medida que utilice un external item de de un código de clasificación del srm.
-- 
-- Añadir propiedad en el commón metadata que indique el código del tipo de anotación que se pondrá asociado al código de la unidad de medida en el srm para indicar la posición del símbolo en el visualizador de indicadores.
-- --------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'metamac.srm.codelist.annotation.type.position_unit','SYMBOL_POSITION',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;


