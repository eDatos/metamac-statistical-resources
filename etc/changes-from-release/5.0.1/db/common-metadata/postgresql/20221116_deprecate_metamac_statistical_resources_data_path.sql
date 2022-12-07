-- --------------------------------------------------------------------------------------------------
-- EDATOS-3103 - Limpiar la propiedad metamac.data.path
-- 
-- Se depreca la propiedad con el valor ${metamac.data.path}/%/docs
-- --------------------------------------------------------------------------------------------------

update tb_data_configurations set conf_key = 'deprecated.metamac.data.docs.statistical_resources.path' where conf_key ='metamac.data.docs.statistical_resources.path';
commit;