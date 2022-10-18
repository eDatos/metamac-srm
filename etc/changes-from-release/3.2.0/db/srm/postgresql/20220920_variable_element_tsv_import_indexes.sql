-- --------------------------------------------------------------------------------------------------
-- EDATOS-3760 - [e-Semántica] El gestor se queda bloqueado al intentar cargar un TSV con cerca de 2000 elementos de variable
-- 
-- Se crean índices para tratar de acelerar la carga de elementos de variable a través de ficheros TSV
-- --------------------------------------------------------------------------------------------------

CREATE INDEX IX_TB_ANNOTABLE_ARTEFACTS_CODE ON TB_ANNOTABLE_ARTEFACTS USING BTREE (CODE);

CREATE INDEX IX_TB_M_VARIABLE_ELEMENTS_IDENTIFIABLE_ARTEFACT_FK ON TB_M_VARIABLE_ELEMENTS USING BTREE (IDENTIFIABLE_ARTEFACT_FK);

CREATE INDEX IX_TB_ANNOTATIONS_TEXT_FK ON TB_ANNOTATIONS USING BTREE (TEXT_FK);

CREATE INDEX IX_TB_ANNOTATIONS_ANNOTABLE_ARTEFACT_FK ON TB_ANNOTATIONS USING BTREE (ANNOTABLE_ARTEFACT_FK);

commit;