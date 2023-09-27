
1) Actualizar la última versión publicada externamente de cada item.
1.1) Ejecutar la siguiente consulta que obtendrá  los distintos esquemas (de concepto, clasificaciones, de tema y de organizaciones) con la última versión que esté publicada externamente. 
/*
  select distinct ON (a.item_scheme_fk) item_scheme_fk, c.code, c.version_logic, ' UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=' || c.id || ';'
 from tb_item_schemes_versions a, tb_annotable_artefacts c 
 where  a.maintainable_artefact_fk = c.id
and public_logic = true 
order by a.item_scheme_fk, c.version_logic desc
*/

1.2) Ejecutar los updates obtenidos en la cuarta columna de la consulta anterior. Serán del tipo
/*
 UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=1;
 UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=1964406;
 UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=2054;
*/


2) Actualizar la última versión publicada internamente de cada item.
2.1) Ejecutar la siguiente consulta que obtendrá  los distintos esquemas (de concepto, clasificaciones, de tema y de organizaciones) con la última versión que esté publicada internamente. 
/*
  select distinct ON (a.item_scheme_fk) item_scheme_fk, c.code, c.version_logic, ' UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=' || c.id || ';'
 from tb_item_schemes_versions a, tb_annotable_artefacts c 
 where  a.maintainable_artefact_fk = c.id
and final_logic = true 
order by a.item_scheme_fk, c.version_logic desc
*/

2.2) Ejecutar los updates obtenidos en la cuarta columna de la consulta anterior. Serán del tipo
/*
 UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=1964406;
 UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=2204;
 UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=2953106;
*/


3) Actualizar la última versión publicada de DSD internamente
3.1) Ejecutar la siguiente consulta que obtendrá  los distintos esquemas (de concepto, clasificaciones, de tema y de organizaciones) con la última versión que esté publicada internamente. 
/*
  select distinct ON (a.structure_fk) structure_fk, c.code, c.version_logic, ' UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=' || c.id || ';'
 from tb_structures_versions a, tb_annotable_artefacts c 
 where  a.maintainable_artefact_fk = c.id
and final_logic = true 
order by a.structure_fk, c.version_logic desc
*/

3.2) Ejecutar los updates obtenidos en la cuarta columna de la consulta anterior. Serán del tipo
/*
 UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=633254;
 UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=1903256;
 UPDATE tb_annotable_artefacts set latest_version_number_final = true where id=662854;
*/

4) Actualizar la última versión publicada de DSD externamente
4.1) Ejecutar la siguiente consulta que obtendrá  los distintos esquemas (de concepto, clasificaciones, de tema y de organizaciones) con la última versión que esté publicada externamente. 
/*
  select distinct ON (a.structure_fk) structure_fk, c.code, c.version_logic, ' UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=' || c.id || ';'
 from tb_structures_versions a, tb_annotable_artefacts c 
 where  a.maintainable_artefact_fk = c.id
and public_logic = true 
order by a.structure_fk, c.version_logic desc
*/

4.2) Ejecutar los updates obtenidos en la cuarta columna de la consulta anterior. Serán del tipo
/*
 UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=633254;
 UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=1903256;
 UPDATE tb_annotable_artefacts set latest_version_number_public = true where id=662854;
*/