-- --------------------------------------------------------------------------------------------------
-- EDATOS-5768 - [srm] Añadir órdenes de visualización a los esquemas de temas.
--  Crear una entrada para el orden alfabético en la tabla TB_M_CAT_SCHEME_ORDER_VIS


 --!!!!!!!Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados
-- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- --------------------------------------------------------------------------------------------------

--1) Compilar función de ordenado alfabético en profundidad que se encuentra en la ruta /etc/helpers/visualisation-order-category-schemes/01-order_function.sql

--2 Añadir extensión si no existe en el esquema extensions
--2.1) Ver si existe en esquema extensions
SELECT *
FROM pg_extension e
JOIN pg_namespace n ON e.extnamespace = n.oid
WHERE e.extname = 'uuid-ossp'
  AND n.nspname = 'extensions';
-- 2.2)  Si no existe crear en ese esquema
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


--3) Crear tabla temporal
create table temp_create_cat_visualisation_order(
category_scheme_id int8 NOT null,
category_scheme_urn varchar(4000),
agency varchar(100),
order_urn varchar(4000)
);

ALTER TABLE temp_create_cat_visualisation_order ADD CONSTRAINT PK_temp_create_cat_visualisation_order PRIMARY KEY (category_scheme_id);
ALTER TABLE temp_create_cat_visualisation_order ADD CONSTRAINT UQ_temp_create_cat_visualisation_order UNIQUE (category_scheme_urn);

--4 Obtener los esquemas de temas afectados y almacenarlos en tabla temporal. Sólo se cogerán aquellos a los que aún no se les haya creado una entrada para el orden alfabético.
--4.1)
 insert into temp_create_cat_visualisation_order(category_scheme_id, category_scheme_urn, agency, order_urn)
 select cv.tb_item_schemes_versions, n.urn,
   (SELECT substring(n.urn FROM 'CategoryScheme=([^:]+)'))
   , 'urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategorySchemeOrder=' || (SELECT substring(n.urn FROM 'CategoryScheme=([^:]+)')) || ':' || n.code || '(' || n.version_logic || ').ALPHABETICAL'
 from tb_cat_schemes_versions cv
 inner join tb_item_schemes_versions iv on cv.tb_item_schemes_versions = iv.id
   inner join tb_annotable_artefacts n on iv.maintainable_artefact_fk = n.id
  where 0 = (select count(*) from TB_M_CAT_SCHEME_ORDER_VIS o1
 inner join tb_cat_schemes_versions cv1 on cv1.tb_item_schemes_versions = o1.category_scheme_fk
  inner join tb_annotable_artefacts n1 on o1.nameable_artefact_fk = n1.id
              and n1.code = 'ALPHABETICAL'
  where cv1.tb_item_schemes_versions = cv.tb_item_schemes_versions );


--5) Crear el orden alfabético para cada esquema de la tabla temp_create_cat_visualisation_order
--5.1 Obtener las sentencias a ejecutar
select
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (GET_NEXT_SEQUENCE_VALUE(''L10NSTRS''), ''' || 'Orden alfabético' || ''', ''es'', GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''L10NSTRS'';
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (GET_NEXT_SEQUENCE_VALUE(''L10NSTRS''), ''' || 'Ordre alfabètic' || ''', ''ca'', GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''L10NSTRS'';
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (GET_NEXT_SEQUENCE_VALUE(''L10NSTRS''), ''' || 'Alphabetical order' || ''', ''en'', GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''), 0);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''L10NSTRS'';
INSERT INTO TB_ANNOTABLE_ARTEFACTS	(ID, UUID, CREATED_DATE_TZ, CREATED_DATE, CREATED_BY, LAST_UPDATED_TZ, LAST_UPDATED, LAST_UPDATED_BY, VERSION, ANNOTABLE_ARTEFACT_TYPE, CODE, URI_PROVIDER, URN, URN_PROVIDER,	NAME_FK, VERSION_LOGIC)
VALUES
	(GET_NEXT_SEQUENCE_VALUE(''ANNOTABLE_ARTEFACTS''), extensions.uuid_generate_v4(),
	 ''Europe/London'', current_timestamp, ''Metamac'', ''Europe/London'', current_timestamp, ''Metamac'',
 	1, ''NAMEABLE_ARTEFACT'', ''ALPHABETICAL'', null,''' || cv.order_urn || ''',''' || cv.order_urn || ''',
 	GET_NEXT_SEQUENCE_VALUE(''I18NSTRS''),
 	null);
INSERT INTO tb_m_cat_scheme_order_vis
(id, update_date_tz, update_date, column_index, "uuid", created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", nameable_artefact_fk, category_scheme_fk)
VALUES(GET_NEXT_SEQUENCE_VALUE(''M_CAT_SCHEME_ORDER_VIS''), ''Europe/London'', current_timestamp, 1,  extensions.uuid_generate_v4(), ''Europe/London'', current_timestamp, ''Metamac'', ''Europe/London'' , current_timestamp, ''Metamac'', 0, GET_NEXT_SEQUENCE_VALUE(''ANNOTABLE_ARTEFACTS''),' || cv.category_scheme_id || ');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''M_CAT_SCHEME_ORDER_VIS'';
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''I18NSTRS'';
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''ANNOTABLE_ARTEFACTS'';'
from temp_create_cat_visualisation_order cv;

-- 5.2 Copiar el resultado como copia avanzada -> texto. Quizás haya que quitar algún caracter especial como el ¶ y la primera fila que es cabecera.

-- 5.3 Ejecutar las sentencias anteriores.

--6) Obtener el orden de los temas asociados al esquema para el campo ORDER1 de la tabla TB_M_CATEGORIES que es donde siempre debe ir el orden alfabético.
--!!Cuidado. Se necesita haber compilado función de paso 1.

DO $$
DECLARE
  scheme_id bigint;
BEGIN
  FOR scheme_id IN
  SELECT category_scheme_id FROM temp_create_cat_visualisation_order
  LOOP
    PERFORM update_order_by_category_scheme_versions(ARRAY[scheme_id]);
  END LOOP;
END;
$$;


--7 Comprobar que no ha quedado ninguno de los temas sin ordenación en la columna "order1" de tb_m_categories. La siguiente consulta no debe devolver entradas
  select c.id, n.urn, cm.order1 from TB_M_CATEGORIES cm
 inner join tb_categories c on c.id = cm.tb_categories
 inner join tb_annotable_artefacts n on c.nameable_artefact_fk = n.id
 where cm.tb_categories in (SELECT cb.ID FROM TB_CATEGORIES cb WHERE cb.ITEM_SCHEME_VERSION_FK in (select category_scheme_id from temp_create_cat_visualisation_order))
 and cm.order1 is null
 order by n.code asc;

--8) Asignar a todos los esquemas de temas el orden alfabético por defecto. Campo DEFAULT_ORDER_VISUAL_FK de TB_M_CAT_SCHEMES_VERSIONS
update TB_M_CAT_SCHEMES_VERSIONS s
   set DEFAULT_ORDER_VISUAL_FK = v.id
  from TB_M_CAT_SCHEME_ORDER_VIS v
 where v.category_scheme_fk = s.tb_cat_schemes_versions
   and v.column_index = 1;

-- 9 borrar tabla temporal
drop table temp_create_cat_visualisation_order;

commit;
