-- --------------------------------------------------------------------------------------------------
-- EDATOS-4869 [srm] Mejora rendimiento borrado internationalStrings
-- 
-- Script  que genera índices por cada constraint a la tabla international_string para la mejora de los rendimientos de borrado de esta tabla.

-- --------------------------------------------------------------------------------------------------

-- INDEXES TO PERFORMANCE THE TB_INTERNATIONAL_STRINGS TABLE DELETES 

CREATE INDEX pk_tb_annotable_artefacts_comment_fk ON tb_annotable_artefacts USING btree (comment_fk);
CREATE INDEX pk_tb_annotable_artefacts_description_fk ON tb_annotable_artefacts USING btree (description_fk);
CREATE INDEX pk_tb_annotable_artefacts_name_fk ON tb_annotable_artefacts USING btree (name_fk);
CREATE INDEX pk_tb_contacts_name_fk ON tb_contacts USING btree (name_fk);
CREATE INDEX pk_tb_contacts_organisation_unit_fk ON tb_contacts USING btree (organisation_unit_fk);
CREATE INDEX pk_tb_contacts_responsibility_fk ON tb_contacts USING btree (responsibility_fk);
CREATE INDEX pk_tb_external_items_title_fk ON tb_external_items USING btree (title_fk);
CREATE INDEX pk_tb_lis_eni_schema_catalogs_description ON tb_lis_eni_schema_catalogs USING btree (description);
CREATE INDEX pk_tb_m_codelists_versions_description_source_fk ON tb_m_codelists_versions USING btree (description_source_fk);
CREATE INDEX pk_tb_m_codelists_versions_short_name_fk ON tb_m_codelists_versions USING btree (short_name_fk);
CREATE INDEX pk_tb_m_codes_short_name_fk ON tb_m_codes USING btree (short_name_fk);
CREATE INDEX pk_tb_m_concepts_acronym_fk ON tb_m_concepts USING btree (acronym_fk);
CREATE INDEX pk_tb_m_concepts_context_fk ON tb_m_concepts USING btree (context_fk);
CREATE INDEX pk_tb_m_concepts_derivation_fk ON tb_m_concepts USING btree (derivation_fk);
CREATE INDEX pk_tb_m_concepts_description_source_fk ON tb_m_concepts USING btree (description_source_fk);
CREATE INDEX pk_tb_m_concepts_doc_method_fk ON tb_m_concepts USING btree (doc_method_fk);
CREATE INDEX pk_tb_m_concepts_legal_acts_fk ON tb_m_concepts USING btree (legal_acts_fk);
CREATE INDEX pk_tb_m_concepts_plural_name_fk ON tb_m_concepts USING btree (plural_name_fk);
CREATE INDEX pk_tb_m_lis_concept_types_description_fk ON tb_m_lis_concept_types USING btree (description_fk);
CREATE INDEX pk_tb_m_quantities_percentage_of_fk ON tb_m_quantities USING btree (percentage_of_fk);
CREATE INDEX pk_tb_m_variable_elements_comment_fk ON tb_m_variable_elements USING btree (comment_fk);
CREATE INDEX pk_tb_m_variable_elements_short_name_fk ON tb_m_variable_elements USING btree (short_name_fk);
CREATE INDEX pk_tb_m_variables_short_name_fk ON tb_m_variables USING btree (short_name_fk);
CREATE INDEX pk_tb_lis_annotation_types_title_fk ON tb_lis_annotation_types USING btree (title_fk);
CREATE INDEX pk_tb_resource_elements_short_name_fk ON tb_resource_elements USING btree (short_name_fk);
CREATE INDEX pk_tb_resource_elements_comment_fk ON tb_resource_elements USING btree (comment_fk);

commit;