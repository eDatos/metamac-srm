-- --------------------------------------------------------------------------------------------------
-- EDATOS-4957 - [srm] Añadir órdenes de visualización a los esquemas de conceptos.
--  Crear una entrada en la tabla TB_M_CONCEPT_SCHEME_ORDER_VIS para el orden "custom" 
--que existe actualmente en la tabla TB_M_CONCEPTS en el campo "order_value" (forma antigua de ordenación)

 --!!!!!!!Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- --------------------------------------------------------------------------------------------------

--1 Añadir extensión si no existe en el esquema extensions
--1.1) Ver si existe en esquema extensions
SELECT *
FROM pg_extension e
JOIN pg_namespace n ON e.extnamespace = n.oid
WHERE e.extname = 'uuid-ossp'
  AND n.nspname = 'extensions';
-- 1.2)  Si no existe crear en ese esquema
/*
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA extensions;

OJO! PUEDE QUE EXISE EN ESQUEMA DE SRM. Comprobar 
SELECT *
FROM pg_extension e
JOIN pg_namespace n ON e.extnamespace = n.oid
WHERE e.extname = 'uuid-ossp';

Y si se quiere pasar a esquema extensions:
--CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;
*/

--2) Crear tabla temporal
create table temp_create_visualisation_custom_order(
concept_scheme_id int8 NOT null,
concept_scheme_urn varchar(4000),
agency varchar(100),
order_urn varchar(4000),
custom_order_name varchar(100), --En cada entorno lo llaman diferente.
custom_order_desc_es varchar(100),
custom_order_desc_en varchar(100),
custom_order_desc_ca varchar(100)
);

ALTER TABLE temp_create_visualisation_custom_order ADD CONSTRAINT PK_temp_create_visualisation_custom_order PRIMARY KEY (concept_scheme_id);
ALTER TABLE temp_create_visualisation_custom_order ADD CONSTRAINT UQ_temp_create_visualisation_custom_order UNIQUE (concept_scheme_urn);

--3 Obtener los esquemas de conceptos afectadosy almacenarlos en tabla temporal. Sólo se cogeran aquellos a los que aún no se les haya creado una entrada para el orden alfabético.
--En función del entorno rellenar el valor para FILL_ME_CUSTOM para el siguiente insert según lo siguiente:
---------si ISTAC -> 'CUSTOM'
---------si IBESTAT -> 'ORDINARIO'
---------si IESTADIS -> 'DEFAULT'

--En función del entorno rellenar la información del orden personalizado para el siguiente insert según lo siguiente:
---------si ISTAC -> 'CUSTOM'           es:Orden personalizado ca: Ordre personalitzat  en: Custom order
---------si IBESTAT -> 'ORDINARIO'     es:Orden estándar ca: Ordre estàndard  en: Standard order
---------si IESTADIS -> 'DEFAULT'       es:Orden por defecto ca: Ordre per defecte  en: Default order

--Por tanto añadir en FILL_ME_CUSTOM_INFO:
-- PARA ISTAC: || 'CUSTOM', 'CUSTOM', 'Orden personalizado', 'Custom order', 'Ordre personalitzat'
--  PARA IBESTAT: || 'ORDINARIO', 'ORDINARIO', 'Orden estándar', 'Standard order', 'Ordre estàndard'
--  PARA IESTADIS:|| 'DEFAULT', 'DEFAULT', 'Orden por defecto', 'Default order', 'Ordre per defecte'   

 insert into temp_create_visualisation_custom_order(concept_scheme_id, concept_scheme_urn, agency, order_urn, custom_order_name, custom_order_desc_es, custom_order_desc_en, custom_order_desc_ca) select cv.tb_item_schemes_versions,  n.urn, (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) 
   ,  'urn:siemac:org.siemac.metamac.infomodel.structuralresources.ConceptSchemeOrder=' || (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) || ':' || n.code || '(' || n.version_logic || ').' FILL_ME_CUSTOM_INFO
 from tb_concept_schemes_versions cv
 inner join tb_item_schemes_versions iv on cv.tb_item_schemes_versions = iv.id
   inner join tb_annotable_artefacts n on iv.maintainable_artefact_fk =  n.id
  where 0 = (select count(*) from TB_M_CONCEPT_SCHEME_ORDER_VIS o1
 inner join tb_concept_schemes_versions cv1 on cv1.tb_item_schemes_versions = o1.concept_scheme_fk
  inner join tb_annotable_artefacts n1 on o1.nameable_artefact_fk =  n1.id
              and n1.code = FILL_ME_CUSTOM
  where cv1.tb_item_schemes_versions = cv.tb_item_schemes_versions );

  /* EJEMPLO PARA ISTAC Y DEMO Y DESARROLLO. SUSTITUIR FILL_ME_CUSTOM POR 'CUSTOM'
 insert into temp_create_visualisation_custom_order(concept_scheme_id, concept_scheme_urn, agency, order_urn, custom_order_name, custom_order_desc_es, custom_order_desc_en, custom_order_desc_ca) select cv.tb_item_schemes_versions,  n.urn, (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) 
   ,  'urn:siemac:org.siemac.metamac.infomodel.structuralresources.ConceptSchemeOrder=' || (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) || ':' || n.code || '(' || n.version_logic || ').' || 'CUSTOM', 'CUSTOM', 'Orden personalizado', 'Custom order', 'Ordre personalitzat'
 from tb_concept_schemes_versions cv
 inner join tb_item_schemes_versions iv on cv.tb_item_schemes_versions = iv.id
   inner join tb_annotable_artefacts n on iv.maintainable_artefact_fk =  n.id
  where 0 = (select count(*) from TB_M_CONCEPT_SCHEME_ORDER_VIS o1
 inner join tb_concept_schemes_versions cv1 on cv1.tb_item_schemes_versions = o1.concept_scheme_fk
  inner join tb_annotable_artefacts n1 on o1.nameable_artefact_fk =  n1.id
              and n1.code = 'CUSTOM'
  where cv1.tb_item_schemes_versions = cv.tb_item_schemes_versions );
  */
  
  /* EJEMPLO PARA IBESTAT. SUSTITUIR FILL_ME_CUSTOM POR 'ORDINARIO'
 insert into temp_create_visualisation_custom_order(concept_scheme_id, concept_scheme_urn, agency, order_urn, custom_order_name, custom_order_desc_es, custom_order_desc_en, custom_order_desc_ca) select cv.tb_item_schemes_versions,  n.urn, (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) 
   ,  'urn:siemac:org.siemac.metamac.infomodel.structuralresources.ConceptSchemeOrder=' || (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) || ':' || n.code || '(' || n.version_logic || ').' || 'ORDINARIO', 'ORDINARIO', 'Orden estándar', 'Standard order', 'Ordre estàndard'
 from tb_concept_schemes_versions cv
 inner join tb_item_schemes_versions iv on cv.tb_item_schemes_versions = iv.id
   inner join tb_annotable_artefacts n on iv.maintainable_artefact_fk =  n.id
  where 0 = (select count(*) from TB_M_CONCEPT_SCHEME_ORDER_VIS o1
 inner join tb_concept_schemes_versions cv1 on cv1.tb_item_schemes_versions = o1.concept_scheme_fk
  inner join tb_annotable_artefacts n1 on o1.nameable_artefact_fk =  n1.id
              and n1.code = 'ORDINARIO'
  where cv1.tb_item_schemes_versions = cv.tb_item_schemes_versions );
  */
  
  /* EJEMPLO PARA IESTADIS. SUSTITUIR FILL_ME_CUSTOM POR 'CUSTOM'
 insert into temp_create_visualisation_custom_order(concept_scheme_id, concept_scheme_urn, agency, order_urn, custom_order_name, custom_order_desc_es, custom_order_desc_en, custom_order_desc_ca) select cv.tb_item_schemes_versions,  n.urn, (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) 
   ,  'urn:siemac:org.siemac.metamac.infomodel.structuralresources.ConceptSchemeOrder=' || (SELECT substring(n.urn FROM 'ConceptScheme=([^:]+)')) || ':' || n.code || '(' || n.version_logic || ').' || 'DEFAULT', 'DEFAULT', 'Orden por defecto', 'Default order', 'Ordre per defecte'
 from tb_concept_schemes_versions cv
 inner join tb_item_schemes_versions iv on cv.tb_item_schemes_versions = iv.id
   inner join tb_annotable_artefacts n on iv.maintainable_artefact_fk =  n.id
  where 0 = (select count(*) from TB_M_CONCEPT_SCHEME_ORDER_VIS o1
 inner join tb_concept_schemes_versions cv1 on cv1.tb_item_schemes_versions = o1.concept_scheme_fk
  inner join tb_annotable_artefacts n1 on o1.nameable_artefact_fk =  n1.id
              and n1.code = 'DEFAULT'
  where cv1.tb_item_schemes_versions = cv.tb_item_schemes_versions );
  */
  
  
--4) Actualizar el orden "custom"  para cada esquema de conceptos
--4.1) El orden custom se pondrá en la columna "order2" de la tabla tb_m_concepts. El 1 está reservado para el orden alfabético.
UPDATE TB_M_CONCEPTS cm
set order2 = order_value
FROM temp_create_visualisation_custom_order t
 inner join tb_concepts c on c.ITEM_SCHEME_VERSION_FK = t.concept_scheme_id
where c.id = cm.tb_concepts; 


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

--7 Asignar a todos los esquemas de conceptos el orden custom por defecto. Campo  DEFAULT_ORDER_VISUAL_FK de TB_M_CONCEPT_SCHEMES_VERSIONS
update TB_M_CONCEPT_SCHEMES_VERSIONS s
   set DEFAULT_ORDER_VISUAL_FK = v.id
  from tb_m_concept_scheme_order_vis v
 where v.concept_scheme_fk = s.tb_concept_schemes_versions
   and  v.column_index = 2;


-- 8 borrar tabla temporal 
drop table temp_create_visualisation_custom_order;


commit;
