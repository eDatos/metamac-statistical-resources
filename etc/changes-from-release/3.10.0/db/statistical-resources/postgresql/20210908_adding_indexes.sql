-- ---------------------------------------------------------------------------------------------------
-- EDATOS-3406 - Migración de datos a Postgresql
-- ---------------------------------------------------------------------------------------------------
-- Se crean índices para la base de datos PostgreSQL de cara a mejorar el rendimiento de la siguiente
-- consulta: 
-- https://git.arte-consultores.com/istac/metamac-statistical-resources/-/blob/develop/metamac-statistical-resources-core/src/main/java/org/siemac/metamac/statistical/resources/core/dataset/repositoryimpl/DatasetVersionRepositoryImpl.java#L170
-- 
-- en memoria a medida que detecta la necesidad.
-- ---------------------------------------------------------------------------------------------------

CREATE INDEX tb_code_dimensions_dataset_version_fk_idx ON tb_code_dimensions USING btree (dataset_version_fk);
CREATE INDEX tb_code_dimensions_dsd_component_id_idx ON tb_code_dimensions USING btree (dsd_component_id);

commit;