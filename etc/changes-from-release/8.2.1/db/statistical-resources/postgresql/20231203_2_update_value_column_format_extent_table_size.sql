-- --------------------------------------------------------------------------------------------------
-- EDATOS-4271 - Mostrar el número de celdas de una tabla
-- 
-- Script para añadir esta información para aquellos datasets existentes actualmente.
-- --------------------------------------------------------------------------------------------------

--ATENCIÓN!!!!!!!! PRECONDICIÓN: se debe haber ejecutado el script relacionado con edatos-4271 "20231024_add_column_format_extent_table_size.sql"

-----------------SCRIPTS A EJECUTAR SOBRE LA BASE DE DATOS STATISTICAL-RESOURCES-DATA
--1) crear la tabla temporal de datasets
create table temp_dataset_observations
(urn varchar(4000),
 observations BIGINT default 1
 );
CREATE INDEX IX_temp_dataset_observations ON temp_dataset_observations(urn);

-- 2) inicializar tabla temporal con todos los datasets existentes
insert into temp_dataset_observations select dataset_id from tb_datasets; 

-- 3) generar updates a tabla temporal
select 'update temp_dataset_observations set observations = observations*(select count(distinct(' || column_name || ')) from ' || table_name || ') where urn = ''' || td.dataset_id || ''';' 
FROM tb_datasets td, tb_dataset_dimensions tdd 
where td.id = tdd.dataset_fk 
order by td.dataset_id; 

--4) ejecutar updates generados en paso anterior tiempo ejecución en local: 2minutos

/*
Ejemplos de salida
update temp_dataset_observations set observations = observations*(select count(distinct(DIMENSION_00)) from DATA_TRANS_MAR_000001_001000) where urn = 'urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=DREM:TRANS_MAR_000001(001.000)';
update temp_dataset_observations set observations = observations*(select count(distinct(DIMENSION_02)) from DATA_TRANS_MAR_000001_001000) where urn = 'urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=DREM:TRANS_MAR_000001(001.000)';
*/

--5) Obtener updates para statistical-resources
select 'update tb_datasets_versions tdv set format_extent_table_size = ' || t.observations || ' from tb_stat_resources tsr where  tdv.siemac_resource_fk = tsr.id and tsr.urn = ''' || t.urn || ''';'
from temp_dataset_observations t where observations is not null and observations > 0;

-----------------FIN SCRIPTS A EJECUTAR SOBRE LA BASE DE DATOS STATISTICAL-RESOURCES-DATA

-----------------SCRIPTS A EJECUTAR SOBRE LA BASE DE DATOS STATISTICAL-RESOURCES

--6) ATENCIÓN!!! Ejecutar resultados del apartado anterior no en esta base de datos sino en la base de datos statistical-resources

--6.1) Hacer commit en bd statistical-resources donde se ejecutaron los scripts de actuallización.
commit;

-----------------FIN SCRIPTS A EJECUTAR SOBRE LA BASE DE DATOS STATISTICAL-RESOURCES

-----------------SCRIPTS A EJECUTAR SOBRE LA BASE DE DATOS STATISTICAL-RESOURCES-DATA

--7) borrar tabla temporal
drop table temp_dataset_observations;

-----------------FIN SCRIPTS A EJECUTAR SOBRE LA BASE DE DATOS STATISTICAL-RESOURCES-DATA