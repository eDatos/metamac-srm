-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4368] Añadir información de limitaciones a servicio web de api de datasets

-- Se actualiza el campo finalLogicClient que no se estaba actualizando por un error en código.
-- --------------------------------------------------------------------------------------------------

update tb_annotable_artefacts a
set final_logic_client = true
from  tb_content_constraints c
where final_logic = true   and  public_logic = true  and a.id = c.maintainable_artefact_fk;  

COMMIT;