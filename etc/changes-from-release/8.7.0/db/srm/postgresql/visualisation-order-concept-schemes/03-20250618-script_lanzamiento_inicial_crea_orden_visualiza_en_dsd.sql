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
dim_component_fk int8 NOT null,
dim_component_urn varchar(4000),
display_concept_scheme_order_fk int8 NOT null

);

ALTER TABLE temp_create_dsd_visualisation_order ADD CONSTRAINT PK_temp_create_dsd_visualisation_order PRIMARY KEY (dsd_id);

  
--3) Introducir datos de dsd en tabla temporal. 
--Se comprobará que no exista ya una entrada asociada en la tabla final tb_m_dim_vis_info. Así el script servirá para futuro y se puede lanzar varias veces. 
INSERT INTO temp_create_dsd_visualisation_order
(dsd_id, dsd_urn, dim_component_fk, dim_component_urn, display_concept_scheme_order_fk)
select sv.id as dsdVersionFk, n.urn, dimN.id as componentCodeFk, dimN.urn,  csOrderVis.id as DisplayConceptSchemeOrderFk from tb_datastructure_versions dv
inner join tb_structures_versions sv on sv.id = dv.tb_structures_versions
inner join tb_annotable_artefacts n on sv.maintainable_artefact_fk = n.id
inner join tb_m_dimension_orders dim on dim.dsd_version_heading_fk = sv.id or dim.dsd_version_stub_fk = sv.id
inner join tb_annotable_artefacts dimN on dim.dim_component_fk = dimN.id
inner join tb_representations r on r.id = dimN.representation_fk
inner join tb_item_schemes_versions dimM on r.concept_scheme_fk = dimM.id
 inner join tb_annotable_artefacts csDimM on dimM.maintainable_artefact_fk =  csDimM.id
 inner join tb_m_concept_scheme_order_vis csOrderVis on dimM.id =  csOrderVis.concept_scheme_fk
  where dimN.annotable_artefact_type = 'MEASURE_DIMENSION'
 and csOrderVis.column_index = 2
 and 0 = (select count(*) from tb_m_dim_vis_info info where info.dsd_version_fk = sv.id and info.display_concept_scheme_order_fk is not null)  
order by n.urn;
--5) Asociar el orden "custom" del esquema de conceptos asociado a la dimensión medida al orden de visualización por defecto en el dsd
--5.1 Obtener las sentencias a ejecutar
select  
'INSERT INTO tb_m_dim_vis_info 
(id, "uuid", "version", dim_component_fk, display_order_fk, hierarchy_levels_open_fk, dsd_version_fk, display_code, display_concept_scheme_order_fk)
VALUES(GET_NEXT_SEQUENCE_VALUE(''M_DIM_VIS_INFO''),  extensions.uuid_generate_v4(), 0, ' || cv.dim_component_fk || ', null, null,' || cv.dsd_id || ', false,' || cv.display_concept_scheme_order_fk || ');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = ''M_DIM_VIS_INFO'';'
from temp_create_dsd_visualisation_order cv;
  
-- 5.2 Copiar el resultado como copia avanzada -> texto. Quizás haya que quitar algún caracter especial como el ¶ y la primera fila que es cabecera.
 
-- 5.3 Ejecutar las sentencias anteriores.
  

--6 Comprobar que todos los dsd tienen un orden por defecto para la dimensión de medida. Para ello ejecutar la consulta del apartado 3 sin el INSERT a la tabla temp_create_dsd_visualisation_order
--Debe devolver 0 entradas. 


-- 7 borrar tabla temporal 
drop table temp_create_dsd_visualisation_order;


commit;
