-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Añadir campo isActivated en tabla tb_geocov_varelem_cache_datasets_versions
-- is_activated boolean NOT NULL DEFAULT true  Campo que indicará si la tabla es válida (TRUE) o si 
-- la entrada está marcada para borrar por parte de la tarea programada indicada en "metamac.statistical_resources.geografic_coverage_cache_clear.cron_expression",
-- por tanto, no debe salir en las consultas que se hagan a la caché.
-- ---------------------------------------------------------------------------------------------------

ALTER TABLE tb_geocov_varelem_cache_datasets_versions_peruebas ADD COLUMN  is_activated boolean not null default true 

commit;