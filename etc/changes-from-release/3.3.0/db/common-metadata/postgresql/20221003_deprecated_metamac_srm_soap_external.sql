-- --------------------------------------------------------------------------------------------------
-- EDATOS-3772 - [e-Semántica] Eliminar proyectos de servicios web soap
-- 
-- Se deprecate la propiedad con la url para los servicios soap
-- --------------------------------------------------------------------------------------------------

update TB_DATA_CONFIGURATIONS set conf_key = 'deprecated.metamac.srm.soap.external' where conf_key = 'metamac.srm.soap.external';
commit;