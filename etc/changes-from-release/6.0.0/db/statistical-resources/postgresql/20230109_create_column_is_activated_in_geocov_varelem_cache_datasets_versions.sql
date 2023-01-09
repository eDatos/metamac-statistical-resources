-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3770 - Añadir campo isActivated en tabla tb_geocov_varelem_cache_datasets_versions
  - is_activated boolean NOT NULL DEFAULT true  Campo que indicará si la tabla es válida (TRUE) o si la entrada está marcada para borrar por parte de la tarea programada indicada en "metamac.statistical_resources.geografic_coverage_cache_clear.cron_expression" y, por tanto, no debe salir en las consultas que se hagan a la caché.
-- ---------------------------------------------------------------------------------------------------

alter table tb_geocov_varelem_cache_datasets_versions add column is_activated boolean

update tb_geocov_varelem_cache_datasets_versions set is_activated =true

alter table tb_geocov_varelem_cache_datasets_versions alter column is_activated set default true

commit;