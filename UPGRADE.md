# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*De esta forma, si tuviéramos una instalación en una versión **A.B.C** y quisieramos actualizar a una versión posterior **X.Y.Z** para la cual existan versiones anteriores que incluyan cambios listados en este documento, se deberá realizar la actualización pasando por todas estas versiones antes de poder llegar a la versión deseada.*

*EJEMPLO: Queremos actualizar desde la versión 1.0.0 a la 3.0.0 y existe un cambio en la base de datos en la actualización de la versión 1.0.0 a la 2.0.0.*

*Se deberá realizar primero la actualización de la versión 1.0.0 a la 2.0.0 y luego desde la 2.0.0 a la 3.0.0*

## 10.19.0 a 10.19.1-SNAPSHOT

* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: etc/changes-from-release/10.19.0/db


* Se debe resetear el schema registry para el topic OPERATION_PUBLICATIONS debido a que se han modificado las
  propiedades de los mensajes que se publican en dicho topic:
  ```shell 
  curl -X DELETE http://localhost:8081/subjects/DATASET_PUBLICATIONS-value
  curl -X DELETE http://localhost:8081/subjects/COLLECTION_PUBLICATIONS-value
  curl -X DELETE http://localhost:8081/subjects/QUERY_PUBLICATIONS-value

  ````
* Se han de borrar los mensajes existentes en el topic DATASET_PUBLICATIONS:
  ```shell 
  /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  DATASET_PUBLICATIONS --add-config retention.ms=100 --alter
  /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  DATASET_PUBLICATIONS --describe retention.ms
  ````
* Esperar 1 minuto antes de volver a restaurar con la siguiente sentencia
  ```shell
   /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  DATASET_PUBLICATIONS --delete-config retention.ms --alter
  ```
* Se han de borrar los mensajes existentes en el topic COLLECTION_PUBLICATIONS:
  ```shell 
  /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  COLLECTION_PUBLICATIONS --add-config retention.ms=100 --alter
  /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  COLLECTION_PUBLICATIONS --describe retention.ms
  ````
* Esperar 1 minuto antes de volver a restaurar con la siguiente sentencia
  ```shell
   /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  COLLECTION_PUBLICATIONS --delete-config retention.ms --alter
  ```
* Se han de borrar los mensajes existentes en el topic QUERY_PUBLICATIONS:
  ```shell 
  /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  QUERY_PUBLICATIONS --add-config retention.ms=100 --alter
  /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  QUERY_PUBLICATIONS --describe retention.ms
  ````
* Esperar 1 minuto antes de volver a restaurar con la siguiente sentencia
  ```shell
   /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name 
  QUERY_PUBLICATIONS --delete-config retention.ms --alter
  ```
## 10.18.2 a 10.18.3-SNAPSHOT
- Se han realizado cambios en la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión.
  Ejecutar los scripts de la siguiente ruta en el esquema correspondiente **EXCEPTO  istac pre e istac pro**: [etc/changes-from-release/10.18.2/db/common-metadata/postgresql/](etc/changes-from-release/10.18.2/db/common-metadata/postgresql)

- Esta versión tiene como dependencia complementos-apps en su versión 8.19.1-SNAPSHOT y metamac-web-common en su versión 5.16.1-SNAPSHOT

## 10.18.0 a 10.18.1
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.18.0/db](etc/changes-from-release/10.18.0/db) 

## 10.17.1 a 10.18.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.17.1/db](etc/changes-from-release/10.17.1/db) 
* Ejecutar el script de la carpeta common-metadata
* Ejecutar los scripts creados en el proyecto  edatos-dataset-repository sobre la base de datos statistical-resources-data: etc/changes-from-release/3.4.0/db/edatos-dataset-repository/postgresql
** Ejecutar los scripts de la carpeta "/db/edatos-dataset-repository/" excepto los de "/db/edatos-dataset-repository/view_transformation_process" que son scripts de adecuación al nuevo modelo por lo que se dejarán para el final.
** Ejecutar los scripts "/db/edatos-dataset-repository/view_transformation_process" siguiendo los pasos que se indican en cada fichero.

## 10.15.2 a 10.16.0
* Se debe resetear el schema registry para el topic JAXI_PUBLICATIONS debido a que se añaden dos nuevos campos
******** curl -X DELETE http://localhost:8081/subjects/JAXI_PUBLICATIONS-value
** Una vez acabe la subida. Al día siguiente comprobar que se ha regenerado automáticamente el schema-registry con los dos nuevos campos. Si no es así habrá que añadirlo manualmente con el xml que se encuentra en la ruta [etc/helpers/kafka/registry_jaxi_publications_topic.json]

* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.15.2/db]
** Se crea variable de entorno que permite deshabilitar el consumidor de jaxi de eTerritorios. De esta manera se podrá lanzar el proceso masivo para eCatalogo sin que se vea afectada la caché de eTerritorios.


## 10.15.1 a 10.15.2
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.15.1/db]


## 10.13.0 a 10.14.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.13.0/db]

## 10.12.0 a 10.13.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.12.0/db]

## 10.11.0 a 10.12.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.11.0/db]

*  Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha situados dentro del proyecto edatos-dataset-repository: etc/changes-from-release/3.2.0/db/edatos-dataset-repository/postgresql
* Se debe realizar un proceso de adecuación de  todas la tablas de datos por lo que se proporciona un script donde se detallan cada uno de los pasos que se deben realizar dentro del proyecto dentro del proyecto edatos-dataset-repository indicado anteriormente.

* Esta versión tiene como dependencia complementos-apps en su versión 8.13.0

## 10.5.1 a 10.6.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.5.1/db]

## 10.1.0 a 10.2.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/10.1.0/db]

## 9.0.3 a 9.1.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/9.0.3/db]

## 8.4.0 a 9.0.0
* **BREAKING CHANGE.** Se quita el formato XLS ya que es un formato con bastantes limitaciones y no se debe usar. Se usa XLSX
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.4.0/db](etc/changes-from-release/8.4.0/db) 

## 8.2.1 a 8.3.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.2.1/db](etc/changes-from-release/8.2.1/db) 
* Para EDATOS-4271 se proporciona un script que obtiene las observaciones reales de un dataset (incluyendo nulos) y las pone en el campo FORMAT_EXTENT_TABLE_SIZE. Los pasos del script se deberán ejecutar en la bd statistical-resources-data para obtener la información. Y ya el resultado final se hará sobre la bd statistical-resources.
* Para la tarea anterior, con el fin de que el ckan actualice los enlaces de xlsx que superen el tamaño máximo permitido para este formato habrá que ejecutar el job de statistical-resources "20231129_update_date_job_resend_dataset_to_publisher_ckan.sql" Ver hora de ejecución en release anterior ya que tarda bastante. 
* Se debe resetear el schema registry para el topic DATASET_PUBLICATIONS debido a que se añade un nuevo campo FORMAT_EXTENT_TABLE_SIZE
******** curl -X DELETE http://localhost:8081/subjects/DATASET_PUBLICATIONS-value


## 8.1.0 a 8.2.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.1.0/db](etc/changes-from-release/8.1.0/db) 

* Se debe resetear el schema registry para el topic DATASET_PUBLICATIONS debido a que se añade un nuevo campo formatExtentObservations
******** curl -X DELETE http://localhost:8081/subjects/DATASET_PUBLICATIONS-value

## 8.0.0 a 8.1.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.0.0/db](etc/changes-from-release/8.0.0/db) 

## 7.0.1 a 8.0.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/7.0.1/db](etc/changes-from-release/7.0.1/db) 

* En edatos-3744 hay una rotura de compatibilidad ya que las fechas "DATE_NEXT_UPDATE" en tabla "TB_DATASETS_VERSIONS"  y "NEXT_VERSION_DATE" en tabla "TB_STAT_RESOURCES" cambian de timestamp a varchar (en formato sdmx) En esta tarea se guardó un backup de los valores en las tablas "TEMP_TB_DATASETS_VERSIONS" y "TEMP_TB_STAT_RESOURCES" respectivamente. 
En esta tarea se deben borrar dichas tablas temporales después de verificar que la migración de datos fue correcta.
Por tanto, pasar script de borrado de ambas tablas que se encuentra en esta ruta:[etc/changes-from-release/7.0.1/db/statistical-resources/20221024_drop_temp_tables.sql]

## 7.0.0 a 7.0.1
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/7.0.0/db](etc/changes-from-release/7.0.0/db) 

## 6.0.0 a 7.0.0
* **BREAKING CHANGE.** Se incorpora un nuevo parámetro opcional a los endpoints de las APIs internas y externas que
  devuelven listados de recursos (ya sean datasets, queries, multidatasets, etc.) denominado `fields`. Se puede consultar
  la documentación de la API para los valores que se le pueden pasar a este parámetro. Es necesario actualizar el resto
  de aplicaciones de eDatos que consuman la API de statistical-resources a través de JAX-RS.

Se debe resetear el schema registry para el topic DATASET_PUBLICATIONS debido a que se añade un nuevo campo visualizerHtmlLink
******** curl -X DELETE http://localhost:8081/subjects/DATASET_PUBLICATIONS-value

La tabla tb_geocov_varelem_cache_datasets_versions cambia de estructura y ya no estará ligada a un dataset existente. 
Se cambiará la tabla. Y luego habrá que lanzar la actualización de toda la caché para cargar todos los datos de nuevo.
La manera de actualizar la caché es como hasta ahora:
 1) Ir a la app de statistical-resources, y en la pantalla inicial aparece el botón "Actualizar caché de territorios"
 2) Pulsar el botón y ejecutará dos tareas.
    - Actualización de la caché a partir de los datasets de base de datos.
    - Actualización de la caché a partir del topic JAXI_PUBLICATIONS. 
 La ejecución completa se realiza en background y puede tardar horas en función del volumen de datasets a procesar .

* Se añaden script relacionados con la actualización de la caché de elementos de variable de un dataset y se añaden dos nuevas propiedades en el common_metadata en este orden
[etc/changes-from-release/6.0.0/db](etc/changes-from-release/6.0.0/db).

NOTAS DE LA VERSIÓN: En esta versión se comienza a consumir el topic JAXI_PUBLICATIONS. Por norma general, es la aplicación productora la responsable de contener las clases AVRO que definen los mensajes, así como las propiedades de configuración. Como en este caso el productor es externo al ecosistema eDatos, se ha tomado la decisión de que sea el statistical-resources el "productor" dentro de eDatos y, por tanto, es donde se almacenarán tanto las propiedades como la clase AVRO que recoge los mensajes de ese topic. Por tanto, cualquier consumidor del esquema JAXI_PUBLICATIONS deberá tener como dependencia al statistical-resources para utilizar dicha clase, al igual que ya lo hacen para el resto de clases AVRO del statistical-resources.

## 5.0.1 a 6.0.0
**BREAKING CHANGE** Para los metadatos "DATE_NEXT_UPDATE" y "NEXT_VERSION_DATE" en la salida de las APIs:
A partir de esta versión los metadatos "date_next_update" y "next_version_date" dejan de ser fechas en formato "dateTime" para convertirse en un formato abierto que cumpla con las especificaciones de sdmx 2.1. Por este motivo, las APIs van a devolver en estos metadatos un InternationalString con la conversión del valor introducido.
Ej: si 2022-M12
Se devuelve:
ns2:dateNextUpdate
<cdomain:text xml:lang="en">12/2022</cdomain:text>
<cdomain:text xml:lang="pt">12/2022</cdomain:text>
<cdomain:text xml:lang="es">12/2022</cdomain:text>
<cdomain:text xml:lang="ca">12/2022</cdomain:text>
</ns2:dateNextUpdate>

* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/5.0.1/db](etc/changes-from-release/5.0.1/db).

*******************************
*** La ejecución de esta tarea requiere una serie de pasos en cada entorno que se detallan a continuación:*******

- 1) Parar las app external-users, search-indexer, indicators y statistical-resources
- 2) Realizar los cambios de base de datos que se indican para transformar los campos fechas en internationalString.
- 3) 
 3.1) Borrar los esquemas existentes para los topics  DATASET_PUBLICATIONS, COLLECTION_PUBLICATIONS y QUERY_PUBLICATIONS para ello ejecutar las siguientes instrucciones desde consola en el tomcat correspondiente:
 
******** curl -X DELETE http://localhost:8081/subjects/DATASET_PUBLICATIONS-value
******** curl -X DELETE http://localhost:8081/subjects/COLLECTION_PUBLICATIONS-value
******** curl -X DELETE http://localhost:8081/subjects/QUERY_PUBLICATIONS-value
3.2) Comprobar que se borran todos los esquemas. Según entorno:
Ver enlaces a la información por entorno en la tarea.

- 4) 
4.1) Borrar los mensajes existentes en los topics DATASET_PUBLICATIONS, COLLECTION_PUBLICATIONS y QUERY_PUBLICATIONS. Para ello, 

-- DATASET_PUBLICATIONS
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name DATASET_PUBLICATIONS --add-config retention.ms=100 --alter
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name DATASET_PUBLICATIONS --describe retention.ms
-- Esperar 1 minuto antes de volver a restaurar con la siguiente sentencia
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name DATASET_PUBLICATIONS --delete-config retention.ms --alter

-- COLLECTION_PUBLICATIONS
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name COLLECTION_PUBLICATIONS --add-config retention.ms=100 --alter
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name COLLECTION_PUBLICATIONS --describe retention.ms
--ESPERAR 1 MINUTOS ANTES DE VOLVER A RESTAURAR CON LA SIGUIENTE SENTENCIA
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name COLLECTION_PUBLICATIONS --delete-config retention.ms --alter

-- QUERY_PUBLICATIONS
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name QUERY_PUBLICATIONS --add-config retention.ms=100 --alter
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name QUERY_PUBLICATIONS --describe retention.ms
--ESPERAR 1 MINUTOS ANTES DE VOLVER A RESTAURAR CON LA SIGUIENTE SENTENCIA
******** /servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 --entity-type topics --entity-name QUERY_PUBLICATIONS --delete-config retention.ms --alter

4.2) Comprobar que se borran todos los datos para el topic DATASET_PUBLICATIONS. Según entorno:
-- (PUEDE TARDAR UN RATO EN BORRAR TODO) 
Ver enlaces a la información por entorno en la tarea.



- 5) 
 5.1) Builds de proyectos
   - metamac-core-common
   - metamac-web-common
 5.2) Actualizar apps
   - metamac-sso
   - statistical-resources   
   - edatos-external-users
   - search-indexers
   - indicators
   - portal
*******************************

6) Reiniciar servicios de kafka (parar y volver a arrancar)

* Aviso de futura deprecación
** Los métodos de la API que recibían el parámetro _dim_ pasarán a recibir el parámetro _representation_ que tiene un formato distinto. El parámetro _dim_ dejará de ser soportado en futuras versiones. Consulte la documentación de la API para más información

## 4.0.0 a 5.0.0
* Rotura de la compatibilidad:
** Debido a refactorización de código al mover determinadas clases de los subproyectos metamac-statistical-resources-rest-external-impl y metamac-statistical-resources-rest-internal-impl al proyecto metamac-statistical-resources-rest-api-common que implicaron el renombrado de paquetes, es necesario que las aplicaciones que usen el proyecto metamac-statistical-resources como librería sean adaptadas al actualizar a esta versión. De no hacerlo, las aplicaciones no compilarán correctamente.
** Se modifica la firma de la API, concretamente el atributo keywords pasa de estar dentro del metadata a la raíz de los recursos
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/4.0.0/db](etc/changes-from-release/4.0.0/db).


## 3.12.0 a 4.0.0
* A partir de esta versión de la aplicación se elimina el soporte para bases de datos Oracle o Sql Server, siendo PostgreSQL la única base de datos con soporte.

## 3.10.2 a 3.11.0
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/3.10.2/db](etc/changes-from-release/3.10.2/db) 
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha situados dentro del proyecto edatos-dataset-repository: etc/changes-from-release/1.1.1/db/edatos-dataset-repository/postgresql

## 3.10.0 a 3.10.1
* Se han realizado cambios en la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/3.10.0/db/statistical-resources/postgresql](etc/changes-from-release/3.10.0/db/statistical-resources/postgresql) 
* Actualizar el WAR

## 3.8.1 a 3.9.0
* Se han realizado cambios que implican que, previo al despliegue de esta versión en cualquier entorno, se debe realizar la migración de Kafka a la versión 6.1.1.
* Se debe modificar el fichero logback-statistical-resources-web.xml para añadir la siguiente entrada justo después del inicio del tag configuration. La siguiente entrada configura un filtro a nivel de logs que evita que se emitan mensajes de logs duplicados de forma indefinida a los que la nueva versión de Kafka es propenso.

~~~
  <turboFilter class="org.siemac.edatos.core.common.util.ExpiringDuplicateMessageFilter">
	<allowedRepetitions>5</allowedRepetitions>
	<cacheSize>500</cacheSize>
	<expireAfterWriteSeconds>900</expireAfterWriteSeconds>
  </turboFilter>
~~~

## 3.8.0 a 3.8.1
* Se han realizado cambios a la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha situados dentro del proyecto edatos-dataset-repository: etc/changes-from-release/1.1.0/db/edatos-dataset-repository/postgresql
* Actualizar el WAR

## 0.0.0 a 3.7.0
* El proceso de actualizaciones entre versiones para versiones anteriores a la 3.7.0 está definido en "Metamac - Manual de instalación.doc"
