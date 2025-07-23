-- --------------------------------------------------------------------------------------------------
-- EDATOS-4957 - [srm] Añadir órdenes de visualización a los esquemas de conceptos.
-- crear secuencia asociada al orden de visualización de esquema de conceptos.
-- --------------------------------------------------------------------------------------------------
INSERT into TB_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_VALUE) VALUES ('M_CONCEPT_SCHEME_ORDER_VIS', 1);

commit;