-- --------------------------------------------------------------------------------------------------
-- EDATOS-4869 [srm] Mejora rendimiento borrado internationalStrings
-- 
-- Se quita índice no necesario.

-- --------------------------------------------------------------------------------------------------

drop index if exists idx_tb_entities_to_delete_id;

commit;