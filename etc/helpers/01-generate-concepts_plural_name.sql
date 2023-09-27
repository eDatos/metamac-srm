-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-4129 - Carga masiva de nombres en plural
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Esta consulta de BBDD permite generar las sentencias de base datos necesarias para informar el metadato nombre en plural de los conceptos.
-- Algunas consideraciones a tener en cuenta:
-- -- El valor informado para el metadato es una copia del valor existente en el metadato nombre del propio concepto que está siendo tratado.
-- -- Dado que el metadato nombre es un international string, el metadato nombre en plurar se informará con tantos localised string como tuviese el primer metadato.
-- -- Los conceptos afectados por este script son aquellos que:
-- -- -- No tienen el metadato nombre en plural informado.
-- -- -- Pertenecen a un esquema de concepto de tipo TRANSVERSAL u OPERACIONAL.
-- El script devuelve como resultado 4 columnas:
-- -- Columna consulta: Las sentencias SQL necesarias para informar el metadato nombre en plural.
-- -- Columna tb_concepts: El identificador autoincremental de BBDD del concepto afectado
-- -- Columna code: El metadato identificador del concepto afectado a fin de identificarlo más claramente.
-- -- Columna pos: La posición relativa de la sentencia SQL a fin de mantener el orden correcto de las sentencias.
-- Este script debe ejecutarse en dos pasos:
-- -- Primer paso: se ejecuta el script presente en este fichero.
-- -- Segundo paso: se ejecutan las sentencias SQL, localizadas en la columna consulta, generadas como resultado de la ejecución del primer paso.
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- NOTA IMPORTANTE:
--
-- Debe revisarse detenidamente las sentencias SQL generadas en la columna consulta antes de ejecutarlas a fin de detectar posibles problemas con el uso de comillas simples
-- o comillas dobles. Puede ser de utilidad revisar el script a través de un programa como DBeaver cuyo editor ayude con el resaltado de la sintaxis de las sentencias.
--
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EJEMPLO DE SQL GENERADO
-- --------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
-- -- Concepto: VINO_CALIDAD Esquema de conceptos: urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=IBESTAT:CSO_000274A(03.000) Tipo: OPERATION
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('L10NSTRS'), "Vi de qualitat de les Illes Balears", 'ca', GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1); UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'L10NSTRS';
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('L10NSTRS'), "Vino de calidad de las Illes Balears", 'es', GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1); UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'L10NSTRS';
-- UPDATE tb_m_concepts SET plural_name_fk = GET_NEXT_SEQUENCE_VALUE('I18NSTRS') WHERE tb_concepts = 79901; UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'I18NSTRS';

-- -- Concepto: MERCADO_COMERCIALIZADOR Esquema de conceptos: urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=IBESTAT:CSO_000274A(03.000) Tipo: OPERATION
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('L10NSTRS'), "Mercat comercialitzador", 'ca', GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1); UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'L10NSTRS';
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('L10NSTRS'), "Mercado comercializador", 'es', GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1); UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'L10NSTRS';
-- UPDATE tb_m_concepts SET plural_name_fk = GET_NEXT_SEQUENCE_VALUE('I18NSTRS') WHERE tb_concepts = 79851; UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'I18NSTRS';

-- Concepto: TIPO_VINO Esquema de conceptos: urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=IBESTAT:CSO_000274A(03.000) Tipo: OPERATION
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('L10NSTRS'), "Tipus de vi", 'ca', GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1); UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'L10NSTRS';
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE('L10NSTRS'), "Tipo de vino", 'es', GET_NEXT_SEQUENCE_VALUE('I18NSTRS'), 1); UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'L10NSTRS';
-- UPDATE tb_m_concepts SET plural_name_fk = GET_NEXT_SEQUENCE_VALUE('I18NSTRS') WHERE tb_concepts = 79801; UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'I18NSTRS';
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------


(select '-- Concepto: ' || taa.code || ' Esquema de conceptos: ' || taa2.urn || ' Tipo: ' || tmcsv.concept_scheme_type as consulta, tmc.tb_concepts, taa.code, 0 as "pos" 
from
	tb_concepts tc,	tb_m_concepts tmc, tb_annotable_artefacts taa, tb_concept_schemes_versions tcsv, tb_m_concept_schemes_versions tmcsv, tb_item_schemes_versions tisv, tb_annotable_artefacts taa2
where
	tmc.tb_concepts = tc.id
	and tmc.plural_name_fk is null
	and tc.nameable_artefact_fk = taa.id
	and tc.item_scheme_version_fk = tisv.id
	and	tmcsv.tb_concept_schemes_versions = tcsv.tb_item_schemes_versions
	and tisv.id = tcsv.tb_item_schemes_versions
	and taa2.id = tisv.maintainable_artefact_fk
	and tmc.tb_concepts in (select tmc.tb_concepts from tb_concepts tc, tb_m_concepts tmc, tb_concept_schemes_versions tcsv, tb_m_concept_schemes_versions tmcsv, tb_item_schemes_versions tisv where tmc.tb_concepts = tc.id and tmc.plural_name_fk is null and tc.item_scheme_version_fk = tisv.id and tmcsv.tb_concept_schemes_versions = tcsv.tb_item_schemes_versions and tisv.id = tcsv.tb_item_schemes_versions and tmcsv.concept_scheme_type in ('OPERATION', 'TRANSVERSAL')))
union
(
select
	'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 1);' as consulta, tmc.tb_concepts, taa.code, 1 as "pos"
from
	tb_concepts tc,	tb_m_concepts tmc, tb_annotable_artefacts taa
where
	tmc.tb_concepts = tc.id
	and tmc.plural_name_fk is null
	and tc.nameable_artefact_fk = taa.id
	and tmc.tb_concepts in (select tmc.tb_concepts from tb_concepts tc, tb_m_concepts tmc, tb_concept_schemes_versions tcsv, tb_m_concept_schemes_versions tmcsv, tb_item_schemes_versions tisv where tmc.tb_concepts = tc.id and tmc.plural_name_fk is null and tc.item_scheme_version_fk = tisv.id and tmcsv.tb_concept_schemes_versions = tcsv.tb_item_schemes_versions and tisv.id = tcsv.tb_item_schemes_versions and tmcsv.concept_scheme_type in ('OPERATION', 'TRANSVERSAL')))
union 
(
select
	'INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE(''L10NSTRS''), "' || tls."label" || '", ''' || tls.locale || ''', GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 1); UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''L10NSTRS'';' as consulta, tmc.tb_concepts, taa.code, 2 as "pos"
from
	tb_concepts tc,	tb_m_concepts tmc, tb_annotable_artefacts taa, tb_international_strings tis, tb_localised_strings tls
where
	tmc.tb_concepts = tc.id
    and tmc.plural_name_fk is null
    and tc.nameable_artefact_fk = taa.id
    and tis.id = taa.name_fk
    and tls.international_string_fk = tis.id
    and tmc.tb_concepts in (select tmc.tb_concepts from tb_concepts tc, tb_m_concepts tmc, tb_concept_schemes_versions tcsv, tb_m_concept_schemes_versions tmcsv, tb_item_schemes_versions tisv where tmc.tb_concepts = tc.id and tmc.plural_name_fk is null and tc.item_scheme_version_fk = tisv.id and tmcsv.tb_concept_schemes_versions = tcsv.tb_item_schemes_versions and tisv.id = tcsv.tb_item_schemes_versions and tmcsv.concept_scheme_type in ('OPERATION', 'TRANSVERSAL')))
union (
select
	'UPDATE tb_m_concepts SET plural_name_fk = GET_NEXT_SEQUENCE_VALUE(''I18NSTRS'') WHERE tb_concepts = ' || tmc.tb_concepts || '; UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''I18NSTRS'';' as consulta, tmc.tb_concepts, taa.code, 3 as "pos"
from
	tb_concepts tc, tb_m_concepts tmc, tb_annotable_artefacts taa
where
	tmc.tb_concepts = tc.id
    and tmc.plural_name_fk is null
    and tc.nameable_artefact_fk = taa.id
    and tmc.tb_concepts in (select tmc.tb_concepts from tb_concepts tc, tb_m_concepts tmc, tb_concept_schemes_versions tcsv, tb_m_concept_schemes_versions tmcsv, tb_item_schemes_versions tisv where tmc.tb_concepts = tc.id and tmc.plural_name_fk is null and tc.item_scheme_version_fk = tisv.id and tmcsv.tb_concept_schemes_versions = tcsv.tb_item_schemes_versions and tisv.id = tcsv.tb_item_schemes_versions and tmcsv.concept_scheme_type in ('OPERATION', 'TRANSVERSAL')))
union (select
	'' as consulta, tmc.tb_concepts, taa.code, 5 as "pos"
from
	tb_concepts tc,	tb_m_concepts tmc, tb_annotable_artefacts taa
where
	tmc.tb_concepts = tc.id
	and tmc.plural_name_fk is null
	and tc.nameable_artefact_fk = taa.id
	and tmc.tb_concepts in (select tmc.tb_concepts from tb_concepts tc, tb_m_concepts tmc, tb_concept_schemes_versions tcsv, tb_m_concept_schemes_versions tmcsv, tb_item_schemes_versions tisv where tmc.tb_concepts = tc.id and tmc.plural_name_fk is null and tc.item_scheme_version_fk = tisv.id and tmcsv.tb_concept_schemes_versions = tcsv.tb_item_schemes_versions and tisv.id = tcsv.tb_item_schemes_versions and tmcsv.concept_scheme_type in ('OPERATION', 'TRANSVERSAL'))
)    
order by tb_concepts desc, pos asc;



