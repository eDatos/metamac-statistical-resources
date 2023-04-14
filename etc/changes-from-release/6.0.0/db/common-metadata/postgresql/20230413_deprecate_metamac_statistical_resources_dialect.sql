-- --------------------------------------------------------------------------------------------------
-- EDATOS-3324 - [CORE] Comprobar si se puede eliminar el dialecto de las propiedades de BBDD de todas las aplicaciones
-- 
-- Se depreca la propiedad con el valor metamac.access_control.db.dialect
-- --------------------------------------------------------------------------------------------------

update tb_data_configurations set conf_key = 'deprecated.metamac.statistical_resources.db.dialect' where conf_key ='metamac.statistical_resources.db.dialect';
commit;

update tb_data_configurations set conf_key = 'deprecated.metamac.statistical_resources.repo.db.dialect' where conf_key ='metamac.statistical_resources.repo.db.dialect';
commit;