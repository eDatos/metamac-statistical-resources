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
  
  
  