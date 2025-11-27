# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*De esta forma, si tuviéramos una instalación en una versión **A.B.C** y quisieramos actualizar a una versión posterior **X.Y.Z** para la cual existan versiones anteriores que incluyan cambios listados en este documento, se deberá realizar la actualización pasando por todas estas versiones antes de poder llegar a la versión deseada.*

*EJEMPLO: Queremos actualizar desde la versión 1.0.0 a la 3.0.0 y existe un cambio en la base de datos en la actualización de la versión 1.0.0 a la 2.0.0.*

*Se deberá realizar primero la actualización de la versión 1.0.0 a la 2.0.0 y luego desde la 2.0.0 a la 3.0.0*


## 8.10.0 a 8.10.1-SNAPSHOT
- Se han realizado cambios en la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
  Ejecutar los scripts de la siguiente ruta en el esquema correspondiente **EXCEPTO  istac pre e istac pro**: [etc/changes-from-release/8.10.0/db/common-metadata/postgresql/](etc/changes-from-release/8.10.0/db/common-metadata/postgresql)

- Esta versión tiene como dependencia complementos-apps en su versión 8.19.1-SNAPSHOT y metamac-web-common en su versión 5.16.1-SNAPSHOT

## 8.9.1 a 8.9.2
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
  * Dejar para el final la ejecución de scripts de migración de la carpeta [etc/changes-from-release/8.9.1/db/srm/postgresql/] 

## 8.7.0 a 8.8.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
  * Dejar para el final la ejecución de scripts de migración de la carpeta [etc/changes-from-release/8.7.0/db/srm/postgresql/visualisation-order-concept-schemes] 
  * Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.7.0/db] 
  * Ejecutar en el orden de ficheros indicado siguiendo los pasos, los scripts que se encuentran en la carpeta  [etc/changes-from-release/8.7.0/db/srm/postgresql/visualisation-order-concept-schemes] 

## 8.6.0 a 8.7.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
  Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.6.0/db]

## 8.4.1 a 8.5.0
* Esta versión tiene como dependencia complementos-apps en su versión 8.13.0
* Cambios en statistic-sdmx-srm. Se añade script con actualización de longitud de campos sdmx

  ```shell
    etc/changes-from-release/4.4.0/db/srm/postgresql/20250520_update_urn_field_length.sql   

## 8.3.3 a 8.4.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
  Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.3.3/db](etc/changes-from-release/8.3.3/db)
  
  
## 8.3.1 a 8.3.2
* Existe un script de la release 8.3.0 de mejora de rendimiento en borrados que fueron ejecutados en PRE y PRO del ISTAC. Pero no en el resto de entornos. Ese script hay que ejecutarlo en el resto de entornos. Por tanto ejecutar en todos los entornos !!!menos en los de ISTAC!!

  ```shell
    etc/changes-from-release/8.2.3/db/srm/postgresql/1_20252802_create_indexes_to_international_strings_constraint.sql
  ```shell

* Hay otro script que hay que ejecutar en todos los entornos (en ISTAC incluido) que borra un índice introducido pero que realmente al final no se va a usar. Borrará el índice si existe:
  ```shell
    etc/changes-from-release/8.2.3/db/srm/postgresql/2_20250603_drop_index.sql
  ```shell


## 8.2.3 a 8.3.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
  Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.2.3/db](etc/changes-from-release/8.2.3/db)



* Script con índices en los campos con foreign key a international string para mejorar el proceso de borrado del job 'DeleteEntitiesMarkedToDeleteJob'
Atención!!! El script "1_20252802_create_indexes_to_international_strings_constraint.sql" donde se crean índices ya se ha ejecutado en desarrollo, demo y pre y pro del ISTAC. Faltaría ejecutar en el resto de clientes.
  ```shell
    etc/changes-from-release/8.2.3/db/srm/postgresql/1_20252802_create_indexes_to_international_strings_constraint.sql
    etc/changes-from-release/8.2.3/db/srm/postgresql/2_20250603_drop_index.sql
  ```shell

## 8.0.1 a 8.1.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
 Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.0.1/db](etc/changes-from-release/8.0.1/db) 
 
 * Cambios en statistic-sdmx-srm. Se añade script con api key de sdmx
  ```shell
    etc/changes-from-release/3.7.0/db/postgresql/20242307_create_constant_properties_in_common_metadata.sql
  ```shell

## 7.0.1 a 7.1.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
 Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/7.0.1/db](etc/changes-from-release/7.0.1/db) 

## 6.4.1 a 7.0.1
* **BREAKING CHANGE.** Se quita el formato XLS ya que es un formato con bastantes limitaciones y no se debe usar. Se usa XLSX
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
 Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/6.4.1/db](etc/changes-from-release/6.4.1/db) 
* Se hace uso del topic CODELIST_PUBLICATION. Se requiere borrar los mensajes para evitar problemas de mensajes antiguos que no tienen campos obligatorios. El consumidor empieza desde el principio y falla al tratarlos. Dado que es información no relevante se borra. Para ello
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name CODELIST_PUBLICATIONS --add-config retention.ms=100 --alter
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name CODELIST_PUBLICATIONS --describe retention.ms
-- Esperar 1 minuto antes de volver a restaurar con la siguiente sentencia
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name CODELIST_PUBLICATIONS --delete-config retention.ms --alter
-- Comprobar que se ha borrado. En kafka-topics-ui de cada entorno ir al topic y comprobar que está vacío.

## 6.4.0 a 6.4.1
* Cambios en statistic-sdmx-srm. Se debe ampliar el campo key_representation en tamaño porque si hay muchos valores se pasa de tamaño. Para ello ejecutar el script de la siguiente ruta del proyecto statistic-sdmx-srm:
  ```shell
    etc/changes-from-release/3.6.0/db/postgresql/20240401_change_length_key_representation_column_in_tb_keyvalues.sql
  ```shell

## 6.3.0 a 6.4.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
 Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/6.3.0/db](etc/changes-from-release/6.3.0/db) 
 
## 6.2.0 a 6.3.0
**BREAKING CHANGE.** Los temas pueden tener varios esquemas eni con EDATOS-4190. Hasta ahora sólo tenían uno. Por lo que el campo eniSchemeType de los dos SW de temas que lo usaban y, en su lugar se define "eniSchemaTypes" que contendrá una lista de esquemas ENI
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
 Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/6.2.0/db](etc/changes-from-release/6.2.0/db) 

* Cambios en statistic-sdmx-srm. Se debe borrar campo deshabilitado en release anterior en TB_ANNOTATIONS "old_type" Para ello ejecutar el script de la siguiente ruta del proyecto statistic-sdmx-srm:
  ```shell
    etc/changes-from-release/3.4.0/db/postgresql/20232709_drop-column-type-in-table-tbAnnotations.sql
  ```shell

## 6.0.0 a 6.1.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
 Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/6.0.0/db](etc/changes-from-release/6.0.0/db) 

## 5.0.1 a 6.0.0
**BREAKING CHANGE.** Se incorpora un nuevo parámetro opcional denominado `fields` al siguiente endpoint:
  * **categoryschemes/{agencyID}/{resourceID}/{version}/categories/{categoryID}**
  * La obtención de la última versión de los diferentes recursos se hará por el último número de versión publicada interna/externamente y no por la última versión publicada como hasta ahora.

* Es necesario ejecutar los scripts SQL contenidos en la carpeta
  ```shell
  etc/changes-from-release/5.0.1/db/common-metadata/postgresql
  ```
 * Es necesario ejecutar los scripts SQL contenidos en la carpeta
  ```shell
  etc/changes-from-release/5.0.1/db/srm/postgresql
  ```
  Uno de los scripts anteriores es un script de adecuación para los diferentes recursos del srm para permitir conocer el último número de versión publicada interna o externamente. Seguir los pasos indicados en el script 20230712_resources_latest_version_new_columns_update.sql para su ejecución. Antes de ejecutar este script ejecutar los cambios siguientes de statistic-sdmx-srm.
  
  
* Cambios en statistic-sdmx-srm. Se han realizado cambios a la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha situados dentro del proyecto statistic-sdmx-srm:
  ```shell
  etc/changes-from-release/3.3.0/db/postgresql/20230602_1_create-table-tbLisAnnotationTypes.sql
  etc/changes-from-release/3.3.0/db/postgresql/20230602_2_annotation_type_adaptation_to_new_data_type.sql
  etc/changes-from-release/3.3.0/db/postgresql/20230602_3_deprecate-column-type-in-table-tbAnnotations.sql
  etc/changes-from-release/3.3.0/db/postgresql/20230711_add_columns_latest_version_number_to_maintainable_artefacts.sql
  etc/changes-from-release/3.3.0/db/postgresql/20230630_remove_duplicated_indexes.sql  
  ```
* Esta versión requiere que se actualicen determinados topics de kafka del srm. Se deben borar los esquemas existentes para los topics  ORGANISATION_SCHEME_PUBLICATIONS-value, CONCEPT_SCHEME_PUBLICATIONS-value, CODELIST_PUBLICATIONS-value, CATEGORY_SCHEME_PUBLICATIONS-value, DATA_STRUCTURE_DEFINITION_PUBLICATIONS-value para ello ejecutar las siguientes instrucciones desde consola en el tomcat correspondiente:
  ```shell
  curl -X DELETE http://localhost:8081/subjects/ORGANISATION_SCHEME_PUBLICATIONS-value
  curl -X DELETE http://localhost:8081/subjects/CONCEPT_SCHEME_PUBLICATIONS-value
  curl -X DELETE http://localhost:8081/subjects/CODELIST_PUBLICATIONS-value
  curl -X DELETE http://localhost:8081/subjects/CATEGORY_SCHEME_PUBLICATIONS-value
  curl -X DELETE http://localhost:8081/subjects/DATA_STRUCTURE_DEFINITION_PUBLICATIONS-value
  ```

## 4.0.0 a 5.0.0
**BREAKING CHANGE.** Se incorpora un nuevo parámetro opcional denominado `fields` a los siguientes endpoints:
  * **/agencyschemes/{agencyID}/{resourceID}/{version}/agencies**
  * **/categoryschemes/{agencyID}/{resourceID}/{version}/categories**
  * **/dataconsumerschemes/{agencyID}/{resourceID}/{version}/dataconsumers**
  * **/dataproviderschemes/{agencyID}/{resourceID}/{version}/dataproviders**
  * **/organisationschemes/{agencyID}/{resourceID}/{version}/organisations**
  * **/organisationunitschemes/{agencyID}/{resourceID}/{version}/organisationunits**

Se puede consultar la documentación de la API para los valores que se le pueden pasar a este parámetro. Es necesario actualizar el resto de aplicaciones 
de eDatos que consuman la API de structural-resources a través de JAX-RS.

Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/4.0.0/db](etc/changes-from-release/4.0.0/db)
 ```shell
 ```shell
  etc/changes-from-release/4.0.0/db/srm/postgresql/20220523_create_master_table_eni_schema_catalog.sql
  etc/changes-from-release/4.0.0/db/srm/postgresql/20220523_create_tb_sequence_eni_schema_catalog.sql
  etc/changes-from-release/4.0.0/db/srm/postgresql/20220523_create_foreign_key_to_eni_schema_catalog.sql
  etc/changes-from-release/4.0.0/db/srm/postgresql/20220523_insert_eni_schemas_values.sql  
  ```

## 3.3.0 a 4.0.0
* Rotura de la compatibilidad:
** Se eliminan los servicios soap
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/3.3.0/db](etc/changes-from-release/3.3.0/db)

## 3.2.0 a 3.3.0
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/3.2.0/db](etc/changes-from-release/3.2.0/db)

## 3.1.0 a 3.2.0


* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/3.1.0/db](etc/changes-from-release/3.1.0/db)
* Es necesario ejecutar los scripts SQL del proyecto metamac-srm contenidos en la carpeta
  ```shell
  etc/changes-from-release/3.1.0/db/srm/postgresql/20220909_update_geographical_granularity.sql
  etc/changes-from-release/3.1.0/db/srm/postgresql/20220707_create_normalization_type_column.sql
  etc/changes-from-release/3.1.0/db/srm/postgresql/20220714_create_source_provider_column.sql
  etc/changes-from-release/3.1.0/db/srm/postgresql/20220725_create_is_extended_code_column.sql
  
  ```
* Se han realizado cambios a la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha situados dentro del proyecto statistic-sdmx-srm:
  ```shell
  etc/changes-from-release/3.0.1/db/postgresql/20220807_add-column-comments-tbcontact.sql
  ```

## 3.0.0 a 3.1.0
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/3.0.0/db](etc/changes-from-release/3.0.0/db)
  ```shell
  etc/changes-from-release/3.0.0/db/srm/postgresql/20220616_add_restriction_objects.sql
  ```

## 2.11.0 a 3.0.0
* A partir de esta versión de la aplicación se elimina el soporte para bases de datos Oracle o Sql Server, siendo PostgreSQL la única base de datos con soporte.
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/2.11.1/db](etc/changes-from-release/2.11.1/db)
  ```shell
  etc/changes-from-release/2.11.1/db/srm/postgresql/20220616_add_restriction_objects.sql
  ```

## 2.10.0 a 2.11.0

* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/2.10.0/db](etc/changes-from-release/2.10.0/db)
* Es necesario ejecutar los scripts SQL del proyecto statistic-sdmx-srm contenidos en la carpeta
  ```shell
  etc/changes-from-release/2.5.1/db/srm/postgresql/20211123_add-international-string-index.sql
  etc/changes-from-release/2.5.1/db/srm/postgresql/20211123_add-string-indexes.sql
  ```

## 2.9.1 a 2.10.0

* Es necesario ejecutar los scripts SQL contenidos en la carpeta
  ```shell
  etc/changes-from-release/2.9.1/db/srm/$BD/20210928_add-column-stream-message-status-to-tb_m_cat_schemes_versions.sql
  etc/changes-from-release/2.9.1/db/srm/$BD/20210928_add-column-stream-message-status-to-tb_m_codelists_versions.sql
  etc/changes-from-release/2.9.1/db/srm/$BD/20210928_add-column-stream-message-status-to-tb_m_concept_schemes_versions.sql
  etc/changes-from-release/2.9.1/db/srm/$BD/20210928_add-column-stream-message-status-to-tb_m_datastructure_versions.sql
  etc/changes-from-release/2.9.1/db/srm/$BD/20210928_add-column-stream-message-status-to-tb_m_org_schemes_versions.sql
  ```
  donde `$DBMS` se corresponde con el sistema de gestión de base de datos que esté utilizando.
* Se debe modificar el fichero logback-srm-web.xml para añadir la siguiente entrada justo después del
  inicio del tag configuration. La siguiente entrada configura un filtro a nivel de logs que evita que se emitan
  mensajes de logs duplicados de forma indefinida a los que la nueva versión de Kafka es propenso.
  ```xml
   <turboFilter class="org.siemac.edatos.core.common.util.ExpiringDuplicateMessageFilter">
      <allowedRepetitions>5</allowedRepetitions>
      <cacheSize>500</cacheSize>
      <expireAfterWriteSeconds>900</expireAfterWriteSeconds>
  </turboFilter>
  ```

## 0.0.0 a 2.8.0
* El proceso de actualizaciones entre versiones para versiones anteriores a la 2.8.0 está definido en "Metamac - Manual de instalación.doc"
