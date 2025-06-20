-- --------------------------------------------------------------------------------------------------
-- EDATOS-4957 - [srm] Añadir órdenes de visualización a los esquemas de conceptos.
--  Crea una entrada en tb_m_dim_vis_info para la dimensión de medida de cada DSD que utilice un esquema de conceptos y le pondrá por defecto el 
-- orden de visualización "custom" establecido en cada esquema de conceptos. 

 --!!!!!!!Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- --------------------------------------------------------------------------------------------------

--1 Añadir extensión si no existe en el esquema extensions.

--2) Crear tabla temporal
create table temp_create_dsd_visualisation_order(
dsd_id int8 NOT null,
dsd_urn varchar(4000),
dim_component_fk int8 NOT null
);

ALTER TABLE temp_create_dsd_visualisation_order ADD CONSTRAINT PK_temp_create_dsd_visualisation_order PRIMARY KEY (dsd_id);

  
--3) Introducir datos de dsd en tabla temporal 
 insert into temp_create_visualisation_order(concept_scheme_id, concept_scheme_urn, agency, order_urn) select cv.tb_item_schemes_versions,  n.urn, (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) 
   ,  'urn:siemac:org.siemac.metamac.infomodel.structuralresources.ConceptSchemeOrder=' || (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) || ':' || n.code || '(' || n.version_logic || ').ALPHABETICAL'
 from tb_concept_schemes_versions cv
 inner join tb_item_schemes_versions iv on cv.tb_item_schemes_versions = iv.id
   inner join tb_annotable_artefacts n on iv.maintainable_artefact_fk =  n.id
  where 0 = (select count(*) from TB_M_CONCEPT_SCHEME_ORDER_VIS o1
 inner join tb_concept_schemes_versions cv1 on cv1.tb_item_schemes_versions = o1.concept_scheme_fk
  inner join tb_annotable_artefacts n1 on o1.nameable_artefact_fk =  n1.id
              and n1.code = 'ALPHABETICAL'
  where cv1.tb_item_schemes_versions = cv.tb_item_schemes_versions );

--5) Crear el orden custom para cada esquema de la tabla temp_create_visualisation_custom_order  
--5.1 Obtener las sentencias a ejecutar
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (GET_NEXT_SEQUENCE_VALUE(''L10NSTRS''), ''' || cv.custom_order_desc_es || ''', ''es'', GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''L10NSTRS'';
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (GET_NEXT_SEQUENCE_VALUE(''L10NSTRS''), ''' || cv.custom_order_desc_ca || ''', ''ca'', GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''L10NSTRS'';
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (GET_NEXT_SEQUENCE_VALUE(''L10NSTRS''), ''' || cv.custom_order_desc_en || ''', ''en'', GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''L10NSTRS'';
INSERT INTO TB_ANNOTABLE_ARTEFACTS	(ID, UUID, CREATED_DATE_TZ, CREATED_DATE, CREATED_BY, LAST_UPDATED_TZ, LAST_UPDATED, LAST_UPDATED_BY, VERSION, ANNOTABLE_ARTEFACT_TYPE, CODE, URI_PROVIDER, URN, URN_PROVIDER,	NAME_FK, VERSION_LOGIC)
VALUES
	(GET_NEXT_SEQUENCE_VALUE(''ANNOTABLE_ARTEFACTS''), uuid_generate_v4(),
	 ''Europe/London'', current_timestamp, ''Metamac'', ''Europe/London'', current_timestamp, ''Metamac'',
 	1, ''NAMEABLE_ARTEFACT'',''' || cv.custom_order_name || ''', null,''' || cv.order_urn || ''',''' || cv.order_urn || ''',
 	GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''),
 	null);
INSERT INTO tb_m_concept_scheme_order_vis 
(id, update_date_tz, update_date, column_index, "uuid", created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", nameable_artefact_fk, concept_scheme_fk)
VALUES(GET_NEXT_SEQUENCE_VALUE(''M_CONCEPT_SCHEME_ORDER_VIS''), ''Europe/London'', current_timestamp, 2,  uuid_generate_v4(), ''Europe/London'', current_timestamp, ''Metamac'', ''Europe/London'' , current_timestamp, ''Metamac'', 0, GET_NEXT_SEQUENCE_VALUE(''ANNOTABLE_ARTEFACTS''),' || cv.concept_scheme_id || ');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''M_CONCEPT_SCHEME_ORDER_VIS'';
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''I18NSTRS'';
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''ANNOTABLE_ARTEFACTS'';'
from temp_create_visualisation_custom_order cv;
  
-- 5.2 Copiar el resultado como copia avanzada -> texto. Quizás haya que quitar algún caracter especial como el ¶ y la primera fila que es cabecera.
 
-- 5.3 Ejecutar las sentencias anteriores.
  

--6 Comprobar que no ha quedado ninguno de los conceptos sin ordenación en la columna "order2" de tb_m_concepts. La siguiente consulta no debe devolver entradas
  select c.id, n.urn, cm.order2, order_value from TB_M_CONCEPTS cm
 inner join tb_concepts c on c.id = cm.tb_concepts 
 inner join tb_annotable_artefacts n on c.nameable_artefact_fk =  n.id
 where TB_CONCEPTS in (SELECT cb.ID FROM TB_CONCEPTS cb WHERE cb.ITEM_SCHEME_VERSION_FK in (select concept_scheme_id from temp_create_visualisation_order))
 and cm.order2 is null
 and cm.order_value is not null
 order by code asc

-- 7 borrar tabla temporal 
drop table temp_create_dsd_visualisation_order;

