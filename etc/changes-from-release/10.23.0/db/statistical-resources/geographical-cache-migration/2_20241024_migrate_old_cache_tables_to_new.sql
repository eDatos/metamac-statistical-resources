-- --------------------------------------------------------------------------------------------------
-- EDATOS-4587 - Añadir nivel de colecciones en eTerritorios
-- 
-- El siguiente proceso se encarga de migrar la antigua tabla de caché de territorios tb_territories_by_geo_cache_resource al nuevo modelo de datos.

-- --------------------------------------------------------------------------------------------------


--PASO 1 MIGRACIÓN DE METADATOS DEL DATASET
INSERT INTO tb_geo_cache_resource
(id, urn, code, type, version, title_fk, operation_code, operation_urn, htmllink, is_external_source, is_last_version, is_activated,
update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by)
select 
nextval('seq_geo_cache_resource'),
 c.urn, 
c.code,
'DATASET',
'0',
c.title_fk,
c.operation_code,
c.operation_urn,
c.htmllink,
c.is_external_source,
c.is_last_version,
c.is_activated,
'Europe/London', current_date, 'Europe/London', current_date,  'METAMAC_ADMIN', 'Europe/London', current_date, 'METAMAC_ADMIN'
FROM tb_geocov_varelem_cache_datasets_versions AS c
INNER JOIN
(
  SELECT urn, max(id) as id
  FROM tb_geocov_varelem_cache_datasets_versions 
  where is_activated = true
  GROUP BY urn
) AS t2  ON c.urn       = t2.urn
   and c.id = t2.id
  and c.is_activated = true;
  
  
  
 --PASO 2 migración de elementos de variable

INSERT INTO tb_territories_by_geo_cache_resource( variable_element_fk, geo_cache_resource_fk) 
select  c.variable_element_fk, tgcrr.id
from tb_geocov_varelem_cache_datasets_versions c
inner join tb_geo_cache_resource tgcrr on c.urn = tgcrr.urn 
where c.is_activated = true;

commit;

--PASO 3 Copia de seguridad de tabla antigua. tb_geocov_varelem_cache_datasets_versions.  
/*
Se realizará copia de seguridad de esta tabla para poder borrarla. Esta tabla debe ser borrada ya que se copiará tal cual a la nueva estructura sin crear nuevas entradas en la tabla tb_external_items para los elementos
de variable asociados. Por tanto hay que borrarlos para que, si se borran en la nueva tabla no de problemas.
*/

--3.1 Exportar la tabla tb_geocov_varelem_cache_datasets_versions a CSV
--3.1.1 Botón derecho sobre la tabla y "Exportar Data"
-- 3.1.2 Seleccionar CSV
-- 3.1.3 En la  pantalla de exportación, en "Exporting settings" al valor NULL string asignarle el valor null si no está así.
-- 3.1.4 Asignar un nombre al archivo de salida donde se guardará el CSV

--3.2 Misma operación con la tabla tb_External_items. Exportar la tabla tb_external_items a CSV
--3.1.1 Botón derecho sobre la tabla y "Exportar Data"
-- 3.1.2 Seleccionar CSV
-- 3.1.3 En la  pantalla de exportación, en "Exporting settings" al valor NULL string asignarle el valor null si no está así.
-- 3.1.4 Asignar un nombre al archivo de salida donde se guardará el CSV

--PASO 4 Borrar tabla  tb_geocov_varelem_cache_datasets_versions
drop table tb_geocov_varelem_cache_datasets_versions;
  
  