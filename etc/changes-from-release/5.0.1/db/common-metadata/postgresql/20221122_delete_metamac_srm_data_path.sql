-- EDATOS-3889 - Eliminar la propiedad metamac.data.path
-- 
-- Se elimina la propiedad con el valor ${metamac.data.path}/%/docs
-- --------------------------------------------------------------------------------------------------

delete from tb_data_configurations  where conf_key ='deprecated.metamac.data.docs.srm.path';
commit;
