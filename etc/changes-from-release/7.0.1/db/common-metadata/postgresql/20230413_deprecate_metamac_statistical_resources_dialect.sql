-- --------------------------------------------------------------------------------------------------
-- EDATOS-2234 - [CORE] Comprobar si se puede eliminar el dialecto de las propiedades de BBDD de todas las aplicaciones
-- 
-- Se depreca las propiedades con los valores metamac.statistical_resources.db.dialect y metamac.statistical_resources.repo.db.dialect
-- --------------------------------------------------------------------------------------------------

update tb_data_configurations set conf_key = 'deprecated.metamac.statistical_resources.db.dialect' where conf_key ='metamac.statistical_resources.db.dialect';

update tb_data_configurations set conf_key = 'deprecated.metamac.statistical_resources.repo.db.dialect' where conf_key ='metamac.statistical_resources.repo.db.dialect';
commit;