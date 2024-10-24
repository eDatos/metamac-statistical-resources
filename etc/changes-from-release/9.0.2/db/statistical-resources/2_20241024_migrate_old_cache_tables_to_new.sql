-- --------------------------------------------------------------------------------------------------
-- EDATOS-4587 - Añadir nivel de colecciones en eTerritorios
-- 
-- El siguiente proceso se encarga de migrar la antigua tabla de caché de territorios tb_territories_by_geo_cache_resource al nuevo modelo de datos.

-- --------------------------------------------------------------------------------------------------

--0 Asegurarse que el resto descripts de base de datos están ejecutados. Deben estar creadas las nuevas tablas de caché.

--1 Proceso de creación de scripts que crean las entradas en las nuevas tablas de caché asociado a las entradas de caché existentes para datasets.
--1.1 Exportar la siguiente consulta a TXT.
-- 1.1.1 Seleccionar la  consulta en dbeaver
-- 1.1.2 desplegar menú Ejecutar (Execute)
-- 1.1.3. Seleccionar submenú Exportar desde consulta (Execute from query)
-- 1.1.4. Seleccionar como tipo de salida "TXT"
-- 1.1.5. Ampliar el fetch size a 100000 que por defecto está en 10000
-- 1.1.6. Seleccionar directorio de salida
-- 1.1.7. Exportar y generará un fichero con las inserciones en el directorio de  salida.
-- 1.1.8. Genera la salida pero con un delimitador entre INSERT.
----1.1.8.1 Quitar la primera línea "|?column?    ".  
----1.1.8.2 Quitar el delimitador "|" sustituyéndolo por "" en un editor de textos (sublime, visual studio code)
----1.1.8.3 Quitar el delimitador "¶" sustituyéndolo por "" en un editor de textos (sublime, visual studio code)
----1.1.8.4 Ir a consola de comandos y ejecutar la siguiente sentencia (donde estén los comandos para el dump. Por eje. en local hay que situarse en carpeta  con dump si no está mapeado ej: E:\program files\PostgreSQL\14\bin )
psql -U "metamac_statistical_resources_bd" -W -h localhost metamac_statistical_resources_bd < E:\mig\<NOMBRE_FICHERO_CREADO>
--EJ:  psql -U "metamac_statistical_resources_bd" -W -h localhost -p 5432 metamac_statistical_resources_bd < E:\mig\temp_mig_geo_cache.txt



select '
INSERT INTO tb_geo_cache_resource
(id, urn, code, type, version, title_fk, operation_code, operation_urn, htmllink, is_external_source, is_last_version, is_activated,
update_date_tz, update_date, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by)' ||
' values (nextval(''seq_geo_cache_resource''),' 
|| '''' || c.urn || ''','
|| '''' || c.code || ''','
|| '''' || 'DATASET' || ''','
|| '0,'
|| c.title_fk || ','
|| '''' || c.operation_code || ''','
|| '''' || c.operation_urn || ''','
|| '''' || c.htmllink || ''','
||  c.is_external_source || ','
||  c.is_last_version || ','
||  c.is_activated || ','
'''Europe/London'', current_date, ''Europe/London'', current_date,  ''METAMAC_ADMIN'', ''Europe/London'', current_date, ''METAMAC_ADMIN'');' ||
'
INSERT INTO tb_territories_by_geo_cache_resource(id, version, variable_element_fk, geo_cache_resource_fk) ' ||
' values(nextval(''seq_territories_by_geo_cache_resource''), 0,'
|| c.variable_element_fk || ', currval(''seq_geo_cache_resource''));'
from tb_geocov_varelem_cache_datasets_versions c
where c.is_activated = true;
