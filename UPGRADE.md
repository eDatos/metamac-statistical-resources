# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*De esta forma, si tuviéramos una instalación en una versión **A.B.C** y quisieramos actualizar a una versión posterior **X.Y.Z** para la cual existan versiones anteriores que incluyan cambios listados en este documento, se deberá realizar la actualización pasando por todas estas versiones antes de poder llegar a la versión deseada.*

*EJEMPLO: Queremos actualizar desde la versión 1.0.0 a la 3.0.0 y existe un cambio en la base de datos en la actualización de la versión 1.0.0 a la 2.0.0.*

*Se deberá realizar primero la actualización de la versión 1.0.0 a la 2.0.0 y luego desde la 2.0.0 a la 3.0.0*

## X.0.0 a Y.0.0
* En edatos-3744 hay una rotura de compatibilidad ya que las fechas "DATE_NEXT_UPDATE" en tabla "TB_DATASETS_VERSIONS"  y "NEXT_VERSION_DATE" en tabla "TB_STAT_RESOURCES" cambian de timestamp a varchar (en formato sdmx) En esta tarea se guardó un backup de los valores en las tablas "TEMP_TB_DATASETS_VERSIONS" y "TEMP_TB_STAT_RESOURCES" respectivamente. 
En esta tarea se deben borrar dichas tablas temporales después de verificar que la migración de datos fue correcta.
Por tanto, pasar script de borrado de ambas tablas que se encuentra en esta ruta:
[etc/changes-from-release/5.0.0/db](etc/changes-from-release/5.0.0/db).

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
