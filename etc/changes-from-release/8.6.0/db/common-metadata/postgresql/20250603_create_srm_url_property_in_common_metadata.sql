-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5038 Enlazar desde portal ciertos recursos cuya visualización se realiza en el srm-visualizer
    -- "metamac.srm.srm-visualizer.url"
-- ---------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.srm.srm-visualizer.url','FILL_ME',true);

-- PRE ISTAC
-- insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.srm.srm-visualizer.url','https://www3-pre.gobiernodecanarias.org/aplicaciones/appsistac/activos-semanticos',false);

-- PRO ISTAC
-- insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.srm.srm-visualizer.url','https://www3.gobiernodecanarias.org/aplicaciones/appsistac/activos-semanticos',false);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;
