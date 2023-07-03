-- EDATOS-38005 - Eliminar la propiedad deprecated.metamac.srm.soap.external
-- 
-- Se elimina la propiedad del common-metadata con valor deprecated.metamac.srm.soap.external. 
-- En edatos-3772 se marcó como obsoleta por lo que se elimina.
-- --------------------------------------------------------------------------------------------------

delete from TB_DATA_CONFIGURATIONS where conf_key = 'deprecated.metamac.srm.soap.external';
commit;