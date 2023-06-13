-- --------------------------------------------------------------------------------------------------
-- EDATOS-4125 - Eliminar la propiedad del dialecto de BBDD
-- 
-- Se elimina las propiedades con los valores metamac.statistical_resources.db.dialect y metamac.statistical_resources.repo.db.dialect
-- --------------------------------------------------------------------------------------------------

delete from tb_data_configurations where conf_key = 'deprecated.metamac.statistical_resources.db.dialect';
delete from tb_data_configurations where conf_key = 'deprecated.metamac.statistical_resources.repo.db.dialect';
commit;