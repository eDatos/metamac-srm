-- --------------------------------------------------------------------------------------------------
-- EDATOS-5768 - [srm] Añadir órdenes de visualización a los esquemas de temas.
-- Crear secuencia asociada al orden de visualización de esquema de temas.
-- --------------------------------------------------------------------------------------------------
INSERT into TB_SEQUENCES(SEQUENCE_NAME, SEQUENCE_NEXT_VALUE) VALUES ('M_CAT_SCHEME_ORDER_VIS', 1);

commit;
