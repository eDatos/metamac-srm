-- Items
CREATE INDEX IX_TB_M_CODES_FK ON TB_M_CODES (TB_CODES);

CREATE INDEX IX_TB_M_CONCEPTS_FK ON TB_M_CONCEPTS (TB_CONCEPTS);
CREATE INDEX IX_TB_M_CONCEPTS_EXTENDS_FK ON TB_M_CONCEPTS (EXTENDS_FK);
CREATE INDEX IX_TB_CONCEPT_ROLES_ROLE_FK ON TB_M_CONCEPT_ROLES (CONCEPT_ROLE_FK);
CREATE INDEX IX_TB_CONCEPT_ROLES_CONCEPT_FK ON TB_M_CONCEPT_ROLES (CONCEPT_FK);
CREATE INDEX IX_TB_M_CONCEPTS_QUANTITY_FK ON TB_M_CONCEPTS (QUANTITY_FK);
CREATE INDEX IX_TB_M_QUANT_NUMERATOR_FK ON TB_M_QUANTITIES(NUMERATOR_FK);
CREATE INDEX IX_TB_M_QUANT_DENOMINATOR_FK ON TB_M_QUANTITIES(DENOMINATOR_FK);
CREATE INDEX IX_TB_M_QUANT_BASE_Q_FK ON TB_M_QUANTITIES(BASE_QUANTITY_FK);
CREATE INDEX IX_TB_M_QUANT_UNIT_CODE_FK ON TB_M_QUANTITIES(UNIT_CODE_FK);
CREATE INDEX IX_TB_M_QUANT_UNIT_MULTIPLIER_FK ON TB_M_QUANTITIES(UNIT_MULTIPLIER_FK);


-- Item schemes
CREATE INDEX IX_TB_M_ORG_SCHEMES_V_FK ON TB_M_ORG_SCHEMES_VERSIONS (TB_ORG_SCHEMES_VERSIONS);
CREATE INDEX IX_TB_M_DATASTRUCTURE_V_FK ON TB_M_DATASTRUCTURE_VERSIONS (TB_DATASTRUCTURE_VERSIONS);

-- Misc
CREATE INDEX IX_TB_ENTITIES_TO_D_TABLE_NAME ON TB_ENTITIES_TO_DELETE (TABLE_NAME);


-- INDEXES TO PERFORMANCE THE TB_INTERNATIONAL_STRINGS TABLE DELETES (for example DeleteEntitiesMarkedToDeleteJob)

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